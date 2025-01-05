@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.BottomSheetState
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.GradientScheme
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent

@Composable
fun RecordingBottomSheet(
    bottomSheetState: BottomSheetState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (recordingTime, isRecording) = when (bottomSheetState) {
        BottomSheetState.Closed -> "" to false
        is BottomSheetState.Pause -> bottomSheetState.recordingTime to false
        is BottomSheetState.Recording -> bottomSheetState.recordingTime to true
    }

    val sheetState = rememberModalBottomSheetState()

    if (bottomSheetState != BottomSheetState.Closed) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(HomeUiEvent.BottomSheetStateChanged(BottomSheetState.Closed)) },
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    isRecording = isRecording,
                    recordingTime = recordingTime
                )
                RecordButtons(
                    modifier = Modifier.padding(vertical = 42.dp, horizontal = 16.dp),
                    isRecording = isRecording,
                    onCancelClick = {},
                    onRecordClick = {},
                    onPauseClick = {}
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
        Text(
            text = recordingTime,
            style = MaterialTheme.typography.labelMedium
        )
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
                    painterResource(R.drawable.ic_recording)
                } else {
                    painterResource(R.drawable.ic_checkmark)
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