package com.example.carlauncher.data

import kotlinx.coroutines.flow.StateFlow

data class TelemetryData(
    val rpm: Int = 0,
    val speed: Int = 0,
    val coolantTemp: Int = 0,
    val batteryVoltage: Float = 0f,
    val oilTemp: Int = 0,
    val intakeTemp: Int = 0,
    val isConnected: Boolean = false
)

interface TelemetryProvider {
    val telemetryFlow: StateFlow<TelemetryData>
    fun start()
    fun stop()
}

class TelemetryRepository(private val provider: TelemetryProvider) {
    val telemetryFlow = provider.telemetryFlow

    fun startListening() {
        provider.start()
    }

    fun stopListening() {
        provider.stop()
    }
}
