package com.serhiiromanchuk.echojournal.presentation.core.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.serhiiromanchuk.echojournal.presentation.theme.EchoSofBlue

object GradientScheme {
    val PrimaryGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF578CFF),
            Color(0xFF1F70F5)
        )
    )
    val DisabledSolidColor = Brush.linearGradient(
        colors = listOf(
            EchoSofBlue,
            EchoSofBlue
        )
    )
}