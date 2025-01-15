package com.serhiiromanchuk.echojournal.presentation.core.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState.Action.Initializing
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState.Action.Paused
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState.Action.Playing
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState.Action.Resumed
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodColor

@Composable
fun MoodPlayer(
    moodColor: MoodColor,
    playerState: PlayerState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    trackWidthChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        color = moodColor.background
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Log.d("MoodPlayer", "amplitude list size - ${playerState.trackDimensions.heightCoefficients}")

            // Play button
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        when (playerState.action) {
                            Initializing -> onPlayClick()
                            Paused -> onResumeClick()
                            Playing -> onPauseClick()
                            Resumed -> onPauseClick()
                        }
                    },
                shape = CircleShape,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = when (playerState.action) {
                        Initializing -> Icons.Default.PlayArrow
                        Paused -> Icons.Default.PlayArrow
                        Playing -> Icons.Default.Pause
                        Resumed -> Icons.Default.Pause
                    },
                    contentDescription = stringResource(R.string.play_button),
                    modifier = Modifier.padding(4.dp),
                    tint = moodColor.button
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(moodColor.track, CircleShape)
                    .onSizeChanged {
                        trackWidthChanged(it.width.toFloat())
                    }
            )

            PlayerTimer(
                duration = playerState.duration,
                currentPosition = playerState.currentPosition,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

@Composable
private fun PlayerTimer(
    duration: String,
    currentPosition: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val placeholderText = if (duration.length > 5) "00:00:00" else "00:00"

        // CurrentPosition
        Box(
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            Text(
                text = currentPosition,
                style = MaterialTheme.typography.labelMedium
            )

            // Hidden placeholder text to define a fixed width.
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Transparent)
            )
        }

        // Duration
        Box(
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            Text(
                text = "/$duration",
                style = MaterialTheme.typography.labelMedium
            )

            // Hidden placeholder text to define a fixed width.
            Text(
                text = "/$placeholderText",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Transparent)
            )
        }
    }
}