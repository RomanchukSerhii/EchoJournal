package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling

import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiEvent
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import java.io.File

sealed interface EntryUiEvent : UiEvent {
    data object BottomSheetClosed : EntryUiEvent
    data class BottomSheetOpened(val mood: MoodUiModel) : EntryUiEvent
    data class MoodSelected(val mood: MoodUiModel) : EntryUiEvent
    data class SheetConfirmedClicked(val mood: MoodUiModel) : EntryUiEvent
    data class TitleValueChanged(val value: String) : EntryUiEvent
    data class TopicValueChanged(val value: String) : EntryUiEvent
    data class DescriptionValueChanged(val value: String) : EntryUiEvent
    data object CreateTopicClicked : EntryUiEvent
    data class TopicClicked(val topic: Topic) : EntryUiEvent
    data class TagClearClicked(val topic: Topic) : EntryUiEvent
    data object PlayClicked : EntryUiEvent
    data object PauseClicked : EntryUiEvent
    data object ResumeClicked : EntryUiEvent
    data class SaveButtonClicked(val outputDir: File) : EntryUiEvent
    data object LeaveDialogToggled : EntryUiEvent
    data object LeaveDialogConfirmClicked : EntryUiEvent
    data object TranscribeButtonClicked : EntryUiEvent
}