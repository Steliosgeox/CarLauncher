package com.steliosgeox.carlauncher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme

/**
 * Large speed readout with animated transitions.
 * Designed to overlay the center of the RPM gauge.
 */
@Composable
fun SpeedDisplay(
    snapshot: TelemetrySnapshot,
    modifier: Modifier = Modifier
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = snapshot.speedKmh,
        animationSpec = tween(durationMillis = 300),
        label = "speed"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = animatedSpeed.toInt().toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "km/h",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E17)
@Composable
private fun SpeedDisplayPreview() {
    CarLauncherTheme {
        SpeedDisplay(snapshot = TelemetrySnapshot(speedKmh = 85f))
    }
}
