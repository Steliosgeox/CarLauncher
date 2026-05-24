package com.steliosgeox.carlauncher.telemetry.core

data class TelemetrySnapshot(
    val timestamp: Long = System.currentTimeMillis(),
    val source: TelemetrySource = TelemetrySource.SIMULATED,
    val speedKmh: Float = 0f,
    val rpm: Float = 0f,
    val coolantTempC: Float = 0f,
    val batteryVoltage: Float = 0f,
    val intakeTempC: Float = 0f,
    val engineLoadPercent: Float = 0f,
    val throttlePercent: Float = 0f,
    val gearDisplay: String = "P",
    val odometerKm: Float = 0f,
    val tripDistanceKm: Float = 0f,
    val driveTimeSeconds: Long = 0,
    val isConnected: Boolean = false
) {
    val rpmRatio: Float get() = (rpm / 7000f).coerceIn(0f, 1f)
}
