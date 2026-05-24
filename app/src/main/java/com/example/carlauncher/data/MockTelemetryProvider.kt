package com.example.carlauncher.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MockTelemetryProvider : TelemetryProvider {
    private val _telemetryFlow = MutableStateFlow(TelemetryData(isConnected = true, batteryVoltage = 14.1f, coolantTemp = 90))
    override val telemetryFlow: StateFlow<TelemetryData> = _telemetryFlow.asStateFlow()

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun start() {
        if (job?.isActive == true) return
        job = scope.launch {
            while (true) {
                // Simulate RPM fluctuation
                val targetRpm = Random.nextInt(800, 3000)
                _telemetryFlow.update { current ->
                    current.copy(
                        rpm = targetRpm,
                        speed = if (targetRpm > 1000) Random.nextInt(10, 60) else 0,
                        oilTemp = Random.nextInt(85, 95),
                        intakeTemp = Random.nextInt(30, 45)
                    )
                }
                delay(200) // Update 5 times a second
            }
        }
    }

    override fun stop() {
        job?.cancel()
    }
}
