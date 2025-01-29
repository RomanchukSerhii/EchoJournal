package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.core.utils.GradientScheme

@Composable
fun ButtonPulsatingCircle(
    baseSize: Dp = 108.dp,
    pulseSize: Dp = 128.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Pulsating Circle Transition")
    val density = LocalDensity.current

    val baseSizePx = with(density) { baseSize.toPx() }
    val pulseSizePx = with(density) { pulseSize.toPx() }

    val animatedSize by infiniteTransition.animateFloat(
        initialValue = baseSizePx,
        targetValue = pulseSizePx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulsating circle size"
    )

    Canvas(
        modifier = modifier
            .size(pulseSize)
    ) {
        // Circle with animation
        drawCircle(
            brush = GradientScheme.FABPulsatingBackground,
            radius = animatedSize / 2,
            center = Offset(size.width / 2, size.height / 2)
        )

        // Circle without animation
        drawCircle(
            brush = GradientScheme.FABRecordingBackground,
            radius = baseSize.toPx() / 2,
            center = Offset(size.width / 2, size.height / 2)
        )
    }
}