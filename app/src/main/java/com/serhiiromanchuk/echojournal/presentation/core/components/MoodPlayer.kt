package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodProvider

@Composable
fun MoodPlayer(
    moodType: MoodType,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        color = MoodProvider.getMoodColor(moodType).background
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Play button
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onPlayClick() },
                shape = CircleShape,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play_button),
                    modifier = Modifier.padding(4.dp),
                    tint = MoodProvider.getMoodColor(moodType).button
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(MoodProvider.getMoodColor(moodType).track, CircleShape)
            )

            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = "0:00/12:30",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}