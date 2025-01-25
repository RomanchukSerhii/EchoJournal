@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.GradientScheme
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeSheetState

@Composable
fun RecordingBottomSheet(
    homeSheetState: HomeSheetState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val sheetState = rememberModalBottomSheetState()

    if (homeSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(HomeUiEvent.StopRecording(saveFile = false)) },
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    isRecording = homeSheetState.isRecording,
                    recordingTime = homeSheetState.recordingTime
                )
                RecordButtons(
                    modifier = Modifier.padding(vertical = 42.dp, horizontal = 16.dp),
                    isRecording = homeSheetState.isRecording,
                    onCancelClick = { onEvent(HomeUiEvent.StopRecording(saveFile = false)) },
                    onRecordClick = {
                        if (homeSheetState.isRecording) {
                            onEvent(HomeUiEvent.StopRecording(saveFile = true))
                        } else {
                            onEvent(HomeUiEvent.ResumeRecording)
                        }
                    },
                    onPauseClick = {
                        if (homeSheetState.isRecording) {
                            onEvent(HomeUiEvent.PauseRecording)
                        } else {
                            onEvent(HomeUiEvent.StopRecording(saveFile = true))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomSheetHeader(
    recordingTime: String,
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title
        Text(
            text = if (isRecording) {
                stringResource(R.string.recording_your_memories)
            } else {
                stringResource(R.string.recording_paused)
            },
            style = MaterialTheme.typography.titleSmall
        )

        // Timer
        Box(
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            Text(
                text = if (recordingTime.length > 5) recordingTime else "00:$recordingTime",
                style = MaterialTheme.typography.labelMedium
            )

            // Hidden placeholder text to define a fixed width.
            Text(
                text = "00:00:00",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Transparent)
            )
        }
    }
}

@Composable
private fun RecordButtons(
    isRecording: Boolean,
    onCancelClick: () -> Unit,
    onRecordClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        // Cancel button
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onCancelClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.cancel_recording)
            )
        }

        // Record button
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(brush = GradientScheme.PrimaryGradient, shape = CircleShape)
                .clickable { onRecordClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = if (isRecording) {
                    painterResource(R.drawable.ic_checkmark)
                } else {
                    painterResource(R.drawable.ic_recording)
                },
                contentDescription = stringResource(R.string.recording_button)
            )
        }

        // Pause button
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onPauseClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Image(
                painter = if (isRecording) {
                    painterResource(R.drawable.ic_pause)
                } else {
                    painterResource(R.drawable.ic_checkmark_primary)
                },
                contentDescription = stringResource(R.string.pause_button)
            )
        }
    }
}