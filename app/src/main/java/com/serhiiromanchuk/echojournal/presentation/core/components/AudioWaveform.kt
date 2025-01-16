package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Composable
fun AudioWaveform(
    amplitudes: List<Float>,
    width: Float,
    spacing: Float,
    playbackPosition: Int,
    totalDuration: Int,
    modifier: Modifier = Modifier,
    colorPlayed: Color = MoodUiModel.Undefined.moodColor.button,
    colorRemaining: Color = MoodUiModel.Undefined.moodColor.track,
    maxAmplitudeHeight: Dp = 16.dp
) {
    val animatedPlaybackPosition = animateFloatAsState(
        targetValue = playbackPosition.toFloat() / totalDuration.toFloat(),
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(maxAmplitudeHeight)
    ) {
        val totalWidth = size.width
        val barWidth = width.toDp().toPx()
        val barSpacing = spacing.toDp().toPx()
        val totalBars = amplitudes.size

        val playbackRatio = animatedPlaybackPosition.value
        val playedBars = playbackRatio * totalBars

        amplitudes.forEachIndexed { index, amplitude ->
            val barHeight = amplitude * maxAmplitudeHeight.toPx()
            val xOffset = index * (barWidth + barSpacing)
            val yOffset = (maxAmplitudeHeight.toPx() - barHeight) / 2
            val color = if (index <= playedBars) colorPlayed else colorRemaining

            if (xOffset + barWidth <= totalWidth) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
            }
        }
    }
}

