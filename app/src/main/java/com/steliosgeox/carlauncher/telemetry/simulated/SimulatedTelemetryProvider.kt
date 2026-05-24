package com.steliosgeox.carlauncher.telemetry.simulated

import com.steliosgeox.carlauncher.telemetry.core.TelemetryProvider
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

class SimulatedTelemetryProvider : TelemetryProvider {

    private val _telemetry = MutableStateFlow(TelemetrySnapshot())
    override val telemetry: Flow<TelemetrySnapshot> = _telemetry.asStateFlow()

    private var scope: CoroutineScope? = null
    private var startTimeMs = 0L
    private var accumulatedDistanceKm = 0f

    override suspend fun start() {
        stop()
        startTimeMs = System.currentTimeMillis()
        accumulatedDistanceKm = 0f
        val newScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        scope = newScope
        newScope.launch { runSimulation() }
    }

    override suspend fun stop() {
        scope?.cancel()
        scope = null
    }

    private suspend fun CoroutineScope.runSimulation() {
        var lastTickMs = System.currentTimeMillis()

        while (isActive) {
            val now = System.currentTimeMillis()
            val dtSeconds = (now - lastTickMs) / 1000f
            lastTickMs = now

            val elapsedMs = now - startTimeMs
            val cycleMs = elapsedMs % CYCLE_DURATION_MS
            val totalElapsedSeconds = (elapsedMs / 1000f)

            val phase = phaseAt(cycleMs)
            val phaseProgress = phaseProgress(cycleMs, phase)

            val speed = speedForPhase(phase, phaseProgress)
            val rpm = rpmForPhase(phase, phaseProgress, speed)
            val gear = gearFromSpeed(speed)
            val throttle = throttleForPhase(phase, phaseProgress)
            val engineLoad = (throttle * 0.8f + rpm / 7000f * 20f).coerceIn(0f, 100f)
            val coolantTemp = coolantTemp(totalElapsedSeconds)
            val batteryVoltage = 13.8f + Random.nextFloat() * 0.5f
            val intakeTemp = 28f + Random.nextFloat() * 10f

            accumulatedDistanceKm += speed / 3600f * dtSeconds

            _telemetry.value = TelemetrySnapshot(
                timestamp = now,
                source = TelemetrySource.SIMULATED,
                speedKmh = speed,
                rpm = rpm,
                coolantTempC = coolantTemp,
                batteryVoltage = batteryVoltage,
                intakeTempC = intakeTemp,
                engineLoadPercent = engineLoad,
                throttlePercent = throttle,
                gearDisplay = gear,
                odometerKm = accumulatedDistanceKm,
                tripDistanceKm = accumulatedDistanceKm,
                driveTimeSeconds = (elapsedMs / 1000),
                isConnected = false
            )

            delay(TICK_MS)
        }
    }

    // --- Phase logic ---

    private enum class Phase { IDLE, ACCELERATING, CRUISING, DECELERATING }

    private fun phaseAt(cycleMs: Long): Phase = when {
        cycleMs < IDLE_MS -> Phase.IDLE
        cycleMs < IDLE_MS + ACCEL_MS -> Phase.ACCELERATING
        cycleMs < IDLE_MS + ACCEL_MS + CRUISE_MS -> Phase.CRUISING
        else -> Phase.DECELERATING
    }

    private fun phaseProgress(cycleMs: Long, phase: Phase): Float = when (phase) {
        Phase.IDLE -> cycleMs.toFloat() / IDLE_MS
        Phase.ACCELERATING -> (cycleMs - IDLE_MS).toFloat() / ACCEL_MS
        Phase.CRUISING -> (cycleMs - IDLE_MS - ACCEL_MS).toFloat() / CRUISE_MS
        Phase.DECELERATING -> (cycleMs - IDLE_MS - ACCEL_MS - CRUISE_MS).toFloat() / DECEL_MS
    }

    // --- Value generators ---

    private fun speedForPhase(phase: Phase, t: Float): Float = when (phase) {
        Phase.IDLE -> 0f
        Phase.ACCELERATING -> lerp(0f, 120f, t)
        Phase.CRUISING -> 100f + sin(t * 6.0).toFloat() * 10f
        Phase.DECELERATING -> lerp(110f, 0f, t)
    }

    private fun rpmForPhase(phase: Phase, t: Float, speed: Float): Float {
        val gearRpm = rpmFromSpeedAndGear(speed)
        return when (phase) {
            Phase.IDLE -> 800f + Random.nextFloat() * 50f
            Phase.ACCELERATING -> {
                val base = gearRpm + sin(t * 12.0).toFloat() * 300f
                base.coerceIn(800f, 5500f)
            }
            Phase.CRUISING -> 2800f + sin(t * 4.0).toFloat() * 200f
            Phase.DECELERATING -> {
                val base = gearRpm - t * 500f
                base.coerceIn(800f, 4000f)
            }
        }
    }

    private fun rpmFromSpeedAndGear(speed: Float): Float = when {
        speed < 5f -> 800f
        speed < 20f -> lerp(1200f, 3500f, (speed - 5f) / 15f)
        speed < 40f -> lerp(1500f, 3800f, (speed - 20f) / 20f)
        speed < 65f -> lerp(1800f, 4200f, (speed - 40f) / 25f)
        speed < 90f -> lerp(2000f, 4500f, (speed - 65f) / 25f)
        else -> lerp(2200f, 3500f, ((speed - 90f) / 30f).coerceIn(0f, 1f))
    }

    private fun throttleForPhase(phase: Phase, t: Float): Float = when (phase) {
        Phase.IDLE -> 0f
        Phase.ACCELERATING -> lerp(30f, 75f, t)
        Phase.CRUISING -> 20f + Random.nextFloat() * 5f
        Phase.DECELERATING -> lerp(10f, 0f, t)
    }

    private fun gearFromSpeed(speed: Float): String = when {
        speed < 5f -> "P"
        speed < 20f -> "1"
        speed < 40f -> "2"
        speed < 65f -> "3"
        speed < 90f -> "4"
        else -> "5"
    }

    private fun coolantTemp(totalSeconds: Float): Float {
        val warmup = (totalSeconds / 30f).coerceIn(0f, 1f)
        val base = lerp(65f, 90f, warmup)
        return base + Random.nextFloat() * 2f - 1f
    }

    private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t.coerceIn(0f, 1f)

    companion object {
        private const val TICK_MS = 80L
        private const val IDLE_MS = 5_000L
        private const val ACCEL_MS = 8_000L
        private const val CRUISE_MS = 6_000L
        private const val DECEL_MS = 5_000L
        private const val CYCLE_DURATION_MS = IDLE_MS + ACCEL_MS + CRUISE_MS + DECEL_MS
    }
}
