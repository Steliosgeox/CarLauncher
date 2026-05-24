package com.steliosgeox.carlauncher.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steliosgeox.carlauncher.telemetry.core.TelemetrySnapshot
import com.steliosgeox.carlauncher.ui.theme.GaugeGreen
import com.steliosgeox.carlauncher.ui.theme.GaugeNeedle
import com.steliosgeox.carlauncher.ui.theme.GaugeOrange
import com.steliosgeox.carlauncher.ui.theme.GaugeRed
import com.steliosgeox.carlauncher.ui.theme.GaugeTrack
import com.steliosgeox.carlauncher.ui.theme.GaugeYellow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RpmGauge(
    snapshot: TelemetrySnapshot,
    modifier: Modifier = Modifier,
    maxRpm: Float = 7000f
) {
    val rpmRatio = (snapshot.rpm / maxRpm).coerceIn(0f, 1f)
    val animatedRpmRatio by animateFloatAsState(
        targetValue = rpmRatio,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "rpmAnimation"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val startAngle = 150f
        val sweepAngle = 240f
        val radius = size.minDimension / 2f * 0.85f
        val cx = size.width / 2f
        val cy = size.height / 2f
        val strokeWidth = size.minDimension * 0.05f

        // Draw background track
        drawArc(
            color = GaugeTrack,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw filled gauge based on current RPM
        val currentSweep = sweepAngle * animatedRpmRatio
        if (currentSweep > 0) {
            // Segments based on ratio (0-40% Green, 40-60% Yellow, 60-80% Orange, 80-100% Red)
            val segments = listOf(
                Pair(0.0f..0.4f, GaugeGreen),
                Pair(0.4f..0.6f, GaugeYellow),
                Pair(0.6f..0.8f, GaugeOrange),
                Pair(0.8f..1.0f, GaugeRed)
            )

            var lastAngle = startAngle
            for ((range, color) in segments) {
                if (animatedRpmRatio > range.start) {
                    val segmentStartRatio = range.start
                    val segmentEndRatio = minOf(animatedRpmRatio, range.endInclusive)
                    val segmentSweep = (segmentEndRatio - segmentStartRatio) * sweepAngle
                    
                    if (segmentSweep > 0) {
                        drawArc(
                            color = color,
                            startAngle = startAngle + (segmentStartRatio * sweepAngle),
                            sweepAngle = segmentSweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = if (segmentEndRatio == animatedRpmRatio) StrokeCap.Round else StrokeCap.Butt),
                            topLeft = Offset(cx - radius, cy - radius),
                            size = Size(radius * 2, radius * 2)
                        )
                    }
                }
            }
        }

        // Draw major ticks
        val numTicks = 8
        for (i in 0 until numTicks) {
            val ratio = i.toFloat() / (numTicks - 1)
            val angle = startAngle + (sweepAngle * ratio)
            val angleRad = Math.toRadians(angle.toDouble()).toFloat()

            val innerRadius = radius - strokeWidth / 2f
            val outerRadius = radius + strokeWidth / 2f + 10.dp.toPx()

            val startX = cx + innerRadius * cos(angleRad)
            val startY = cy + innerRadius * sin(angleRad)
            val endX = cx + outerRadius * cos(angleRad)
            val endY = cy + outerRadius * sin(angleRad)

            drawLine(
                color = Color.White.copy(alpha = 0.7f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 3.dp.toPx()
            )

            // Draw label
            val textRadius = outerRadius + 20.dp.toPx()
            val textX = cx + textRadius * cos(angleRad)
            val textY = cy + textRadius * sin(angleRad)

            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 40f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
                drawText(i.toString(), textX, textY + 14f, paint)
            }
        }

        // Draw minor ticks
        for (i in 0 until (numTicks - 1)) {
            val ratio = (i.toFloat() + 0.5f) / (numTicks - 1)
            val angle = startAngle + (sweepAngle * ratio)
            val angleRad = Math.toRadians(angle.toDouble()).toFloat()

            val innerRadius = radius - strokeWidth / 2f
            val outerRadius = radius + strokeWidth / 2f + 5.dp.toPx()

            val startX = cx + innerRadius * cos(angleRad)
            val startY = cy + innerRadius * sin(angleRad)
            val endX = cx + outerRadius * cos(angleRad)
            val endY = cy + outerRadius * sin(angleRad)

            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Draw needle
        if (animatedRpmRatio > 0) {
            val needleAngle = startAngle + (sweepAngle * animatedRpmRatio)
            val needleAngleRad = Math.toRadians(needleAngle.toDouble()).toFloat()
            val needleOuterRadius = radius - strokeWidth / 2f

            drawLine(
                color = GaugeNeedle,
                start = Offset(cx, cy),
                end = Offset(cx + needleOuterRadius * cos(needleAngleRad), cy + needleOuterRadius * sin(needleAngleRad)),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Center dot
        drawCircle(
            color = Color.DarkGray,
            radius = 16.dp.toPx(),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.LightGray,
            radius = 16.dp.toPx(),
            center = Offset(cx, cy),
            style = Stroke(width = 2.dp.toPx())
        )

        // x1000 RPM text
        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.LTGRAY
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }
            drawText("x1000 RPM", cx, cy + radius * 0.5f, paint)
        }
    }
}

@Preview(widthDp = 400, heightDp = 400)
@Composable
fun PreviewRpmGauge() {
    com.steliosgeox.carlauncher.ui.theme.CarLauncherTheme {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.background(Color(0xFF0A0E17))
        ) {
            RpmGauge(snapshot = TelemetrySnapshot(rpm = 3500f))
        }
    }
}
