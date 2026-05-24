package com.steliosgeox.carlauncher.telemetry.core

import kotlinx.coroutines.flow.Flow

interface TelemetryProvider {
    val telemetry: Flow<TelemetrySnapshot>
    suspend fun start()
    suspend fun stop()
}
