package com.steliosgeox.carlauncher.telemetry.obd

import com.steliosgeox.carlauncher.telemetry.core.TelemetryProvider
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Direct ELM327 Bluetooth OBD-II provider.
 * Sprint 2: Will connect via RFCOMM to ELM327 adapter and poll standard PIDs.
 */
class ObdTelemetryProvider : TelemetryProvider {
    override val telemetry: Flow<TelemetrySnapshot> = emptyFlow()
    override suspend fun start() { TODO("Sprint 2: ELM327 Bluetooth RFCOMM implementation") }
    override suspend fun stop() { TODO("Sprint 2: ELM327 Bluetooth RFCOMM implementation") }
}
