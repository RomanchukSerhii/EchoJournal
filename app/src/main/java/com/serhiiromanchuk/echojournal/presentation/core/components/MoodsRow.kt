package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Composable
fun MoodsRow(
    moods: List<MoodUiModel>,
    activeMood: MoodUiModel,
    onMoodClick: (MoodUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { moodUiModel ->
            MoodItem(
                iconRes = if (activeMood == moodUiModel) {
                    moodUiModel.moodIcons.fill
                } else {
                    moodUiModel.moodIcons.outline
                },
                title = moodUiModel.title,
                onClick = { onMoodClick(moodUiModel) }
            )
        }
    }
}

@Composable
private fun MoodItem(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = title,
            modifier = Modifier.height(40.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
    }
}