package com.steliosgeox.carlauncher.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliosgeox.carlauncher.ui.theme.CardBorder
import com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme

/**
 * Bordered pill showing the current gear (P, R, N, D, 1-6).
 * Positioned below the speed readout in the gauge center.
 */
@Composable
fun GearIndicator(
    gear: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Text(
            text = gear,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E17)
@Composable
private fun GearIndicatorPreview() {
    CarLauncherTheme {
        GearIndicator(gear = "D")
    }
}
