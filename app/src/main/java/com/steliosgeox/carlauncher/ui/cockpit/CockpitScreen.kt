package com.steliosgeox.carlauncher.ui.cockpit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import com.steliosgeox.carlauncher.ui.components.ControlButton
import com.steliosgeox.carlauncher.ui.components.GearIndicator
import com.steliosgeox.carlauncher.ui.components.RpmGauge
import com.steliosgeox.carlauncher.ui.components.SimulationBadge
import com.steliosgeox.carlauncher.ui.components.SpeedDisplay
import com.steliosgeox.carlauncher.ui.components.StatusCard
import com.steliosgeox.carlauncher.ui.components.StatusRow
import com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CockpitScreen(
    viewModel: CockpitViewModel = viewModel()
) {
    val snapshot by viewModel.telemetry.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        val isCompact = maxWidth < 900.dp
        val isExpanded = maxWidth >= 1400.dp

        // Central Map Placeholder + Gauge
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!isCompact) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeftPanel(
                        snapshot = snapshot,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                    )
                    
                    CenterPanel(
                        snapshot = snapshot,
                        modifier = Modifier.weight(if (isExpanded) 1.5f else 1.2f)
                    )
                    
                    RightPanel(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                    )
                }
            } else {
                CenterPanel(
                    snapshot = snapshot,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }

        // Top Status Bar
        StatusBar(
            snapshot = snapshot,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Bottom Control Bar
        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LeftPanel(snapshot: TelemetrySnapshot, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusCard(title = "ENGINE STATUS", modifier = Modifier.fillMaxWidth()) {
            StatusRow("Coolant", "${snapshot.coolantTempC.toInt()}°C")
            StatusRow("Intake", "${snapshot.intakeTempC.toInt()}°C")
            StatusRow("Battery", String.format("%.1fV", snapshot.batteryVoltage))
            StatusRow("Load", "${snapshot.engineLoadPercent.toInt()}%")
            StatusRow("Throttle", "${snapshot.throttlePercent.toInt()}%")
        }

        StatusCard(title = "TRIP INFO", modifier = Modifier.fillMaxWidth()) {
            StatusRow("Trip", String.format("%.1f km", snapshot.tripDistanceKm))
            val mm = snapshot.driveTimeSeconds / 60
            val ss = snapshot.driveTimeSeconds % 60
            StatusRow("Time", String.format("%02d:%02d", mm, ss))
            StatusRow("Odometer", "${snapshot.odometerKm.toInt()} km")
        }
    }
}

@Composable
private fun CenterPanel(snapshot: TelemetrySnapshot, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Map Placeholder behind gauge
        Canvas(modifier = Modifier.fillMaxSize(0.8f)) {
            // Subtle grid
            val step = 40.dp.toPx()
            for (i in 0..(size.width / step).toInt()) {
                drawLine(
                    color = Color.White.copy(alpha = 0.03f),
                    start = Offset(i * step, 0f),
                    end = Offset(i * step, size.height)
                )
            }
            for (i in 0..(size.height / step).toInt()) {
                drawLine(
                    color = Color.White.copy(alpha = 0.03f),
                    start = Offset(0f, i * step),
                    end = Offset(size.width, i * step)
                )
            }
        }
        Text(
            text = "MAP - Coming in Sprint 3",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp)
        )

        // Gauge
        RpmGauge(snapshot = snapshot, modifier = Modifier.fillMaxSize())
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            SpeedDisplay(snapshot = snapshot)
            Spacer(modifier = Modifier.height(16.dp))
            GearIndicator(gear = snapshot.gearDisplay)
        }
    }
}

@Composable
private fun RightPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusCard(title = "NAVIGATION", modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Coming in Sprint 3",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }

        StatusCard(title = "NOW PLAYING", modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Coming in Sprint 2",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun StatusBar(snapshot: TelemetrySnapshot, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimulationBadge(source = snapshot.source)

        var currentTime by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            while (true) {
                currentTime = format.format(Date())
                delay(1000)
            }
        }

        Text(
            text = currentTime,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = String.format("%.1fV", snapshot.batteryVoltage),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(label = "◀◀", onClick = { })
        Spacer(modifier = Modifier.width(32.dp))
        ControlButton(label = "▶", onClick = { })
        Spacer(modifier = Modifier.width(32.dp))
        ControlButton(label = "▶▶", onClick = { })
        Spacer(modifier = Modifier.width(64.dp))
        ControlButton(label = "⚙", onClick = { })
    }
}

@Preview(widthDp = 1024, heightDp = 600, name = "Compact 1024x600")
@Composable
fun PreviewCockpitCompact() {
    CarLauncherTheme {
        CockpitScreen()
    }
}

@Preview(widthDp = 1280, heightDp = 800, name = "Medium 1280x800")
@Composable
fun PreviewCockpitMedium() {
    CarLauncherTheme {
        CockpitScreen()
    }
}

@Preview(widthDp = 1920, heightDp = 1080, name = "Expanded 1920x1080")
@Composable
fun PreviewCockpitExpanded() {
    CarLauncherTheme {
        CockpitScreen()
    }
}
