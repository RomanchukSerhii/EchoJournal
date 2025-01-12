package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.core.utils.GradientScheme

@Composable
fun HomeFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        shape = CircleShape,
        shadowElevation = if (isPressed.value) 7.dp else 6.dp,
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 56.dp,
                    minHeight = 56.dp,
                )
                .clip(CircleShape)
                .background(
                    brush = GradientScheme.PrimaryGradient,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}