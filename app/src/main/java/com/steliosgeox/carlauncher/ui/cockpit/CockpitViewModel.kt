package com.steliosgeox.carlauncher.ui.cockpit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import com.steliosgeox.carlauncher.telemetry.simulated.SimulatedTelemetryProvider
import com.steliosgeox.carlauncher.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Drives the cockpit screen with live telemetry and theme state.
 * Creates a [SimulatedTelemetryProvider] and collects its flow into an
 * observable [StateFlow] for the UI layer.
 */
class CockpitViewModel : ViewModel() {

    private val provider = SimulatedTelemetryProvider()

    private val _telemetry = MutableStateFlow(TelemetrySnapshot())
    val telemetry: StateFlow<TelemetrySnapshot> = _telemetry.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.NIGHT)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
        viewModelScope.launch {
            provider.start()
            provider.telemetry.collect { snapshot ->
                _telemetry.value = snapshot
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            provider.stop()
        }
    }
}
