package com.example.carlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.carlauncher.theme.CarLauncherTheme
import com.example.carlauncher.ui.screens.MainLauncherScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Immersive mode for Car Launcher
    WindowCompat.setDecorFitsSystemWindows(window, false)
    enableEdgeToEdge()

    setContent {
      CarLauncherTheme { 
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { 
            MainLauncherScreen() 
        } 
      }
    }
  }
}
