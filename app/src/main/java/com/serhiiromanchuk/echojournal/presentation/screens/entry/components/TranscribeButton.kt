package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Composable
fun TranscribeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = MoodUiModel.Undefined.moodColor.button,
    size: Dp = 44.dp
) {
    Surface(
        modifier = modifier
            .size(size)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_transcribe),
            contentDescription = "Transcribe button",
            modifier = Modifier.padding(10.dp),
            tint = iconColor
        )
    }
}