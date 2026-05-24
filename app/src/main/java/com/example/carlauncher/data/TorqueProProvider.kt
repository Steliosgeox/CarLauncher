package com.example.carlauncher.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.prowl.torque.remote.ITorqueService

class TorqueProProvider(private val context: Context) : TelemetryProvider {
    private val _telemetryFlow = MutableStateFlow(TelemetryData())
    override val telemetryFlow: StateFlow<TelemetryData> = _telemetryFlow.asStateFlow()

    private var torqueService: ITorqueService? = null
    private var isBound = false
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            torqueService = ITorqueService.Stub.asInterface(service)
            isBound = true
            startPolling()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            torqueService = null
            isBound = false
            stopPolling()
        }
    }

    override fun start() {
        val intent = Intent()
        intent.setClassName("org.prowl.torque", "org.prowl.torque.remote.TorqueService")
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun stop() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
        stopPolling()
    }

    private fun startPolling() {
        if (job?.isActive == true) return
        job = scope.launch {
            while (isBound) {
                try {
                    torqueService?.let { service ->
                        val isConnected = service.isConnectedToECU()
                        if (isConnected) {
                            val rpm = service.getValueForPid("0C", true).toInt()
                            val speed = service.getValueForPid("0D", true).toInt()
                            val coolant = service.getValueForPid("05", true).toInt()
                            
                            _telemetryFlow.update { current ->
                                current.copy(
                                    isConnected = true,
                                    rpm = rpm,
                                    speed = speed,
                                    coolantTemp = coolant
                                )
                            }
                        } else {
                            _telemetryFlow.update { it.copy(isConnected = false) }
                        }
                    }
                } catch (e: Exception) {
                    // Handle RemoteException or polling errors
                }
                delay(200) // Poll 5 times a second
            }
        }
    }

    private fun stopPolling() {
        job?.cancel()
    }
}
