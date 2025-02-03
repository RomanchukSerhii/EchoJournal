package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.core.utils.AmplitudeCalculator
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Composable
fun AudioWaveform(
    amplitudeLogFilePath: String,
    playbackPosition: Int,
    totalDuration: Int,
    modifier: Modifier = Modifier,
    amplitudeWidth: Dp = 4.dp,
    amplitudeSpacing: Dp = 2.5.dp,
    colorPlayed: Color = MoodUiModel.Undefined.moodColor.button,
    colorRemaining: Color = MoodUiModel.Undefined.moodColor.track,
    maxAmplitudeHeight: Dp = 18.dp
) {
    val density = LocalDensity.current
    var totalWidth by remember { mutableFloatStateOf(0f) }
    var correctedSpacing by remember { mutableFloatStateOf(0f) }
    var amplitudeHeightCoefficients by remember { mutableStateOf(listOf<Float>()) }

    // FEEDBACK: Property can be managed by VM to avoid side effect
    LaunchedEffect(totalWidth) {
        if (totalWidth > 0) {
            // FEEDBACK: Logic does not belong in UI
            val amplitudeCalculator = AmplitudeCalculator(
                amplitudeLogFilePath = amplitudeLogFilePath,
                trackWidth = totalWidth,
                amplitudeWidth = with(density) { amplitudeWidth.toPx() },
                spacing = with(density) { amplitudeSpacing.toPx() }
            )
            correctedSpacing = amplitudeCalculator.correctedSpacing()
            amplitudeHeightCoefficients = amplitudeCalculator.heightCoefficients()
        }
    }

    val animatedPlaybackPosition = animateFloatAsState(
        targetValue = playbackPosition.toFloat() / totalDuration.toFloat(),
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(maxAmplitudeHeight)
    ) {
        totalWidth = size.width
        val amplitudeWidthPx = amplitudeWidth.toPx()
        val totalAmplitudes = amplitudeHeightCoefficients.size

        val playbackRatio = animatedPlaybackPosition.value
        val playedBars = playbackRatio * totalAmplitudes

        amplitudeHeightCoefficients.forEachIndexed { index, heightCoefficient ->
            val amplitudeHeight = heightCoefficient * maxAmplitudeHeight.toPx()
            val xOffset = index * (amplitudeWidthPx + correctedSpacing)
            val yOffset = (maxAmplitudeHeight.toPx() - amplitudeHeight) / 2
            val color = if (index < playedBars) colorPlayed else colorRemaining

            if (xOffset + amplitudeWidthPx <= totalWidth) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(amplitudeWidthPx, amplitudeHeight),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
            }
        }
    }
}

