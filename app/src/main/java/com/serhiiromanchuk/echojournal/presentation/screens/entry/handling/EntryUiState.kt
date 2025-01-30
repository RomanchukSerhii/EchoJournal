package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiState
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

data class EntryUiState(
    val currentMood: MoodUiModel = MoodUiModel.Undefined,
    val titleValue: String = "",
    val topicValue: String = "",
    val descriptionValue: String = "",
    val playerState: PlayerState = PlayerState(),
    val currentTopics: List<Topic> = emptyList(),
    val foundTopics: List<Topic> = emptyList(),
    val entrySheetState: EntrySheetState = EntrySheetState(),
    val showLeaveDialog: Boolean = false
) : UiState {
    val isSaveButtonEnabled: Boolean
        get() = titleValue.isNotBlank() && currentMood != MoodUiModel.Undefined

    @Stable
    data class EntrySheetState (
        val isOpen: Boolean = true,
        val activeMood: MoodUiModel = MoodUiModel.Undefined,
        val moods: List<MoodUiModel> = MoodUiModel.allMoods
    )
}