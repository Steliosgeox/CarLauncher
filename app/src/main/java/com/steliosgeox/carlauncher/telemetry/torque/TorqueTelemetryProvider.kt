package com.steliosgeox.carlauncher.telemetry.torque

import com.steliosgeox.carlauncher.telemetry.core.TelemetryProvider
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Torque Pro AIDL integration provider.
 * Sprint 2: Will bind to Torque Pro service and read live PID data via AIDL.
 */
class TorqueTelemetryProvider : TelemetryProvider {
    override val telemetry: Flow<TelemetrySnapshot> = emptyFlow()
    override suspend fun start() { TODO("Sprint 2: Torque Pro AIDL binding implementation") }
    override suspend fun stop() { TODO("Sprint 2: Torque Pro AIDL binding implementation") }
}
