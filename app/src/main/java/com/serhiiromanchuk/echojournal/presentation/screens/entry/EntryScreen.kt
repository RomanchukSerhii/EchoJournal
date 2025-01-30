package com.serhiiromanchuk.echojournal.presentation.screens.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.AppTopBar
import com.serhiiromanchuk.echojournal.presentation.core.components.MoodPlayer
import com.serhiiromanchuk.echojournal.presentation.core.components.TopicDropdown
import com.serhiiromanchuk.echojournal.presentation.core.utils.toDp
import com.serhiiromanchuk.echojournal.presentation.core.utils.toInt
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryBottomButtons
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryTextField
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.LeaveDialog
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.MoodChooseButton
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.TopicTagsRow
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.TranscribeButton
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiState

@Composable
fun EntryScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    audioFilePath: String,
    amplitudeLogFilePath: String,
    entryId: Long
) {
    val viewModel: EntryViewModel =
        hiltViewModel<EntryViewModel, EntryViewModel.EntryViewModelFactory> { factory ->
            factory.create(audioFilePath, amplitudeLogFilePath, entryId)
        }

    BaseContentLayout(
        modifier = modifier,
        viewModel = viewModel,
        actionsEventHandler = { _, actionEvent ->
            when (actionEvent) {
                EntryActionEvent.NavigateBack -> navigationState.popBackStack()
            }
        },
        topBar = {
            AppTopBar(
                title = if (entryId < 0) {
                    stringResource(R.string.new_entry)
                } else {
                    stringResource(R.string.edit_entry)
                },
                onBackClick = { viewModel.onEvent(EntryUiEvent.LeaveDialogToggled) }
            )
        },
        bottomBar = { uiState ->
            val context = LocalContext.current
            EntryBottomButtons(
                primaryButtonText = stringResource(R.string.save),
                onCancelClick = { viewModel.onEvent(EntryUiEvent.LeaveDialogToggled) },
                onConfirmClick = {
                    val outputDir = context.filesDir
                    viewModel.onEvent(EntryUiEvent.SaveButtonClicked(outputDir!!))
                },
                modifier = Modifier.padding(16.dp),
                primaryButtonEnabled = uiState.isSaveButtonEnabled
            )
        },
        onBackPressed = { viewModel.onEvent(EntryUiEvent.LeaveDialogToggled) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { uiState ->
        EntryScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
        EntryBottomSheet(
            entrySheetState = uiState.entrySheetState,
            onEvent = viewModel::onEvent
        )
        if (uiState.showLeaveDialog) {
            LeaveDialog(
                headline = stringResource(R.string.cancel_recording),
                onConfirm = { viewModel.onEvent(EntryUiEvent.LeaveDialogConfirmClicked) },
                onDismissRequest = { viewModel.onEvent(EntryUiEvent.LeaveDialogToggled) },
                supportingText = stringResource(R.string.leave_dialog_supporting_text),
                cancelButtonName = stringResource(R.string.cancel),
                confirmButtonName = stringResource(R.string.leave)
            )
        }
    }
}

@Composable
private fun EntryScreen(
    uiState: EntryUiState,
    onEvent: (EntryUiEvent) -> Unit
) {
    Box {
        var topicOffset by remember { mutableStateOf(IntOffset.Zero) }

        // Will be used to calculate the y-axis offset of the topicOffset
        val verticalSpace = 16.dp.toInt()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(verticalSpace.toDp())
        ) {
            // AddTitle text field
            EntryTextField(
                value = uiState.titleValue,
                onValueChange = { onEvent(EntryUiEvent.TitleValueChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                hintText = stringResource(R.string.add_title),
                leadingIcon = {
                    MoodChooseButton(
                        mood = uiState.currentMood,
                        onClick = { onEvent(EntryUiEvent.BottomSheetOpened(uiState.currentMood)) }
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium,
                iconSpacing = 6.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MoodPlayer(
                    moodColor = uiState.currentMood.moodColor,
                    playerState = uiState.playerState,
                    onPlayClick = { onEvent(EntryUiEvent.PlayClicked) },
                    onPauseClick = { onEvent(EntryUiEvent.PauseClicked) },
                    onResumeClick = { onEvent(EntryUiEvent.ResumeClicked) },
                    modifier = Modifier.height(44.dp).weight(1f),
                )

                TranscribeButton(
                    onClick = {
                        if (uiState.descriptionValue.isBlank()) {
                            onEvent(EntryUiEvent.TranscribeButtonClicked)
                        }
                    },
                    iconColor = uiState.currentMood.moodColor.button
                )
            }


            TopicTagsRow(
                value = uiState.topicValue,
                onValueChange = { onEvent(EntryUiEvent.TopicValueChanged(it)) },
                topics = uiState.currentTopics,
                onTagClearClick = { onEvent(EntryUiEvent.TagClearClicked(it)) },
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        topicOffset = IntOffset(
                            coordinates.positionInParent().x.toInt(),
                            coordinates.positionInParent().y.toInt() + coordinates.size.height + verticalSpace
                        )
                    }
                    .onFocusChanged {
                        onEvent(EntryUiEvent.TopicValueChanged(""))
                    }
            )

            // AddDescription field
            EntryTextField(
                value = uiState.descriptionValue,
                onValueChange = { onEvent(EntryUiEvent.DescriptionValueChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                hintText = stringResource(R.string.add_description),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                singleLine = false
            )
        }

        TopicDropdown(
            searchQuery = uiState.topicValue,
            topics = uiState.foundTopics,
            onTopicClick = { onEvent(EntryUiEvent.TopicClicked(it)) },
            onCreateClick = { onEvent(EntryUiEvent.CreateTopicClicked) },
            startOffset = topicOffset
        )
    }
}