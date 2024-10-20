package com.rwmobi.githubcidemo.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RealisticSpinningWheel(
    segments: List<String>,
    segmentColors: List<Color>,
    centerIcon: Painter,
    onSegmentSelected: (String) -> Unit,
) {
    val numberOfSegments = segments.size
    var isSpinning by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    var selectedSegment by remember { mutableStateOf("") }

    // DecayAnimationSpec to simulate physics-based animation
    val decayAnimationSpec = remember { exponentialDecay<Float>(frictionMultiplier = 1.0f) }

    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            val targetRotation = (0..3600).random().toFloat() // Random target for longer spin

            val result = rotation.animateDecay(
                initialVelocity = targetRotation,
                animationSpec = decayAnimationSpec
            )

            // Determine selected segment once spinning stops
            val normalizedRotation = rotation.value % 360
            val anglePerSegment = 360f / numberOfSegments
            val segmentIndex = (normalizedRotation / anglePerSegment).toInt()
            selectedSegment = segments[segmentIndex]
            onSegmentSelected(selectedSegment)
            isSpinning = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        // Wheel with segments
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .graphicsLayer(rotationZ = rotation.value)
        ) {
            val canvasSize = size.minDimension
            val radius = canvasSize / 2
            val anglePerSegment = 360f / numberOfSegments

            for (i in 0 until numberOfSegments) {
                val startAngle = i * anglePerSegment
                drawSegment(
                    color = segmentColors[i % segmentColors.size],
                    startAngle = startAngle,
                    sweepAngle = anglePerSegment,
                    radius = radius,
                    size = size
                )
                // Draw text in each segment
                drawTextOnPath(
                    text = segments[i],
                    radius = radius,
                    angle = startAngle + anglePerSegment / 2,
                    size = size
                )
            }
        }

        // Center icon that rotates with the wheel
        Image(
            painter = centerIcon,
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop
        )

        // Button to start/stop the spinning wheel
        Button(
            onClick = { if (!isSpinning) isSpinning = true },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(if (isSpinning) "Spinning..." else "Spin")
        }
    }

    // Display selected segment
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Selected Segment: $selectedSegment",
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(16.dp)
        )
    }
}

// Helper function to draw individual segments
fun DrawScope.drawSegment(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float,
    size: Size,
) {
    val path = Path().apply {
        moveTo(size.width / 2, size.height / 2)
        arcTo(
            rect = Rect(Offset.Zero, size),
            startAngleDegrees = startAngle,
            sweepAngleDegrees = sweepAngle,
            forceMoveTo = false
        )
        close()
    }
    drawPath(path, color, style = Stroke(width = 2f))
    drawPath(path, color)
}

// Helper function to draw text on each segment
fun DrawScope.drawTextOnPath(
    text: String,
    radius: Float,
    angle: Float,
    size: Size,
) {
    val centerX = size.width / 2
    val centerY = size.height / 2

    val x = centerX + radius / 2 * cos(Math.toRadians(angle.toDouble())).toFloat()
    val y = centerY + radius / 2 * sin(Math.toRadians(angle.toDouble())).toFloat()

    drawContext.canvas.nativeCanvas.drawText(
        text,
        x,
        y,
        android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 40f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    )
}
