package com.example.carlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carlauncher.ui.MainViewModel
import com.example.carlauncher.ui.components.CentralDial
import com.example.carlauncher.data.TelemetryData
import com.example.carlauncher.media.MediaState

@Composable
fun MainLauncherScreen(viewModel: MainViewModel = viewModel()) {
    val telemetryState by viewModel.telemetryState.collectAsState()
    val mediaState by viewModel.mediaState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Dark premium background
    ) {
        // Main Content Area (Widgets + Dial)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Widgets
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VehicleStatusWidget(telemetryState)
                PlaceholderWidget("Trip Summary\nSpeed: ${telemetryState.speed} km/h")
            }

            // Central Dial
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                CentralDial(rpm = telemetryState.rpm, maxRpm = 8000)
            }

            // Right Widgets
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlaceholderWidget("Navigation")
                MediaWidget(mediaState)
            }
        }

        // Bottom Climate Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF1E293B)), // Slightly lighter dark
            contentAlignment = Alignment.Center
        ) {
            Text("Climate Control Bar", color = Color.White)
        }
    }
}

@Composable
fun MediaWidget(state: MediaState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFF1E293B), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Column {
            Text("Now Playing", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(state.title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text(state.artist, color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun VehicleStatusWidget(data: TelemetryData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFF1E293B), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Column {
            Text("Vehicle Status", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Coolant: ${data.coolantTemp} °C", color = Color.LightGray)
            Text("Battery: ${data.batteryVoltage} V", color = Color.LightGray)
            Text("Oil Temp: ${data.oilTemp} °C", color = Color.LightGray)
        }
    }
}

@Composable
fun PlaceholderWidget(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFF1E293B), shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}
