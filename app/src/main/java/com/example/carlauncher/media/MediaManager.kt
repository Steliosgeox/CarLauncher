package com.example.carlauncher.media

import android.content.Context
import android.media.session.MediaSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MediaState(
    val title: String = "No Media",
    val artist: String = "",
    val isPlaying: Boolean = false
)

class MediaManager(private val context: Context) {
    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    // Note: Requires android.permission.BIND_NOTIFICATION_LISTENER_SERVICE in AndroidManifest
    // and the user must grant notification access to the app to read active media sessions.
    
    fun startListening() {
        // Implementation for ActiveMediaSessionListener goes here.
        // For now, we mock the state:
        _mediaState.value = MediaState("Neon Skyline", "Cyberpunk Mix", true)
    }

    fun stopListening() {
        // Cleanup listener
    }
}
