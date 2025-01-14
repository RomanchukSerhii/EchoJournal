package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state

import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

data class EntryUiState(
    val currentMood: MoodUiModel = MoodUiModel.Undefined,
    val titleValue: String = "",
    val topicValue: String = "",
    val descriptionValue: String = "",
    val playerState: PlayerState = PlayerState(),
    val currentTopics: List<Topic> = listOf(),
    val foundTopics: List<Topic> = listOf(),
    val entrySheetState: EntrySheetState = EntrySheetState()
) : UiState {
    val isSaveButtonEnabled: Boolean
        get() = titleValue.isNotBlank() && currentMood != MoodUiModel.Undefined
}