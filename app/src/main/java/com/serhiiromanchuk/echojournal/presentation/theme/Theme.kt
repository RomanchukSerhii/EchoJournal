package com.serhiiromanchuk.echojournal.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView

private val LightColorScheme = lightColorScheme(
    primary = EchoBlue,
    background = EchoLightBlue,
    surface = Color.White,
    surfaceVariant = EchoExtraLightGray,
    onPrimary = Color.White,
    onSurface = EchoDark,
    onSurfaceVariant = EchoGrayBlue,
    secondary = EchoDarkSteel,
    outline = EchoMutedGray,
    outlineVariant = EchoLightGray
)

@Composable
fun EchoJournalTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}