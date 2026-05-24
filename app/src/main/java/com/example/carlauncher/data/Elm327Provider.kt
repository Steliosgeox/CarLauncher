package com.example.carlauncher.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class Elm327Provider(
    private val context: Context,
    private val deviceMacAddress: String
) : TelemetryProvider {

    private val _telemetryFlow = MutableStateFlow(TelemetryData())
    override val telemetryFlow: StateFlow<TelemetryData> = _telemetryFlow.asStateFlow()

    private var socket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val sppUuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SPP UUID

    @SuppressLint("MissingPermission")
    override fun start() {
        if (job?.isActive == true) return
        job = scope.launch {
            try {
                val adapter = BluetoothAdapter.getDefaultAdapter()
                val device: BluetoothDevice = adapter.getRemoteDevice(deviceMacAddress)
                
                socket = device.createRfcommSocketToServiceRecord(sppUuid)
                socket?.connect()
                
                inputStream = socket?.inputStream
                outputStream = socket?.outputStream

                // Initialize ELM327
                sendCommand("ATZ")   // Reset
                sendCommand("ATE0")  // Echo off
                sendCommand("ATL0")  // Linefeeds off
                sendCommand("ATSP0") // Auto protocol

                _telemetryFlow.value = _telemetryFlow.value.copy(isConnected = true)

                // Start Polling Loop
                while (socket?.isConnected == true) {
                    val rpm = readPid("010C")
                    // Note: RPM is ((A*256)+B)/4
                    val parsedRpm = parseHex(rpm) / 4
                    
                    _telemetryFlow.value = _telemetryFlow.value.copy(
                        rpm = parsedRpm
                    )
                    delay(200)
                }

            } catch (e: Exception) {
                _telemetryFlow.value = _telemetryFlow.value.copy(isConnected = false)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        try {
            socket?.close()
        } catch (e: Exception) {}
        _telemetryFlow.value = _telemetryFlow.value.copy(isConnected = false)
    }

    private fun sendCommand(cmd: String) {
        outputStream?.write((cmd + "\r").toByteArray())
        // Wait for prompt '>'
        delay(100) // basic delay, needs proper stream reading
    }

    private fun readPid(pid: String): String {
        sendCommand(pid)
        val buffer = ByteArray(1024)
        val bytes = inputStream?.read(buffer) ?: 0
        return String(buffer, 0, bytes).trim()
    }

    private fun parseHex(hexStr: String): Int {
        // Simplified parser for demonstration
        return try {
            val cleanStr = hexStr.replace(" ", "").replace("\r", "")
            if (cleanStr.length >= 4) {
                cleanStr.substring(cleanStr.length - 4).toInt(16)
            } else 0
        } catch (e: Exception) {
            0
        }
    }
}
