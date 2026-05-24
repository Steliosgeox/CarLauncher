package com.steliosgeox.carlauncher.diagnostics

data class DiagnosticLog(
    val timestamp: Long,
    val level: DiagnosticLevel,
    val source: String,
    val message: String
)
