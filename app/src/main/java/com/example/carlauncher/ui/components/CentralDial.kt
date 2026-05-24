package com.example.carlauncher.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CentralDial(
    rpm: Int,
    maxRpm: Int = 8000,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .aspectRatio(1f) // Ensure it's circular
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f
        val strokeWidth = 30.dp.toPx()
        
        // Background track
        drawArc(
            color = Color.DarkGray.copy(alpha = 0.3f),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )

        // RPM Gradient Arc
        val rpmGradient = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF22C55E), // Green
                Color(0xFFEAB308), // Yellow
                Color(0xFFEF4444)  // Red
            ),
            center = center
        )

        // Calculate dynamic RPM sweep angle
        val rpmRatio = (rpm.toFloat() / maxRpm).coerceIn(0f, 1f)
        val currentRpmSweep = 270f * rpmRatio

        drawArc(
            brush = rpmGradient,
            startAngle = 135f,
            sweepAngle = currentRpmSweep,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )
    }
}
