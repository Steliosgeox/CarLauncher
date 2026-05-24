package com.example.carlauncher.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlauncher.data.MockTelemetryProvider
import com.example.carlauncher.data.TelemetryRepository
import com.example.carlauncher.media.MediaManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // In a real app, inject this repository via DI (Hilt/Dagger)
    private val telemetryRepository = TelemetryRepository(MockTelemetryProvider())
    private val mediaManager = MediaManager(application)

    val telemetryState = telemetryRepository.telemetryFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = telemetryRepository.telemetryFlow.value
        )
        
    val mediaState = mediaManager.mediaState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = mediaManager.mediaState.value
        )

    init {
        telemetryRepository.startListening()
        mediaManager.startListening()
    }

    override fun onCleared() {
        super.onCleared()
        telemetryRepository.stopListening()
        mediaManager.stopListening()
    }
}
