package com.steliosgeox.carlauncher.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySource
import com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme

private val SimBadgeBackground = Color(0xFFD97706).copy(alpha = 0.25f)
private val SimBadgeText = Color(0xFFFBBF24)

/**
 * Compact amber chip that appears only when telemetry is simulated.
 * Fades and scales in/out so it doesn't pop jarringly on source change.
 */
@Composable
fun SimulationBadge(
    source: TelemetrySource,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    AnimatedVisibility(
        visible = source == TelemetrySource.SIMULATED,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
        modifier = modifier
    ) {
        Text(
            text = if (compact) "SIM" else "SIMULATED",
            style = MaterialTheme.typography.labelMedium,
            color = SimBadgeText,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(SimBadgeBackground)
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E17)
@Composable
private fun SimulationBadgePreview() {
    CarLauncherTheme {
        SimulationBadge(source = TelemetrySource.SIMULATED)
    }
}
