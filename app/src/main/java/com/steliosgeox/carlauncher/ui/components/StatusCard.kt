package com.steliosgeox.carlauncher.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliosgeox.carlauncher.ui.theme.CardBackground
import com.steliosgeox.carlauncher.ui.theme.CardBorder
import com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme

/**
 * Dark card with subtle border for side-panel content.
 * Uses a slot API so callers compose whatever rows they need inside.
 */
@Composable
fun StatusCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing
            )
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E17)
@Composable
private fun StatusCardPreview() {
    CarLauncherTheme {
        StatusCard(title = "Engine Status") {
            StatusRow(label = "Coolant", value = "90", unit = "°C")
            StatusRow(label = "Battery", value = "14.2", unit = "V")
        }
    }
}
