package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

sealed interface EntryUiEvent : UiEvent {
    data object BottomSheetClosed : EntryUiEvent
    data class BottomSheetOpened(val mood: MoodUiModel) : EntryUiEvent
    data class MoodSelected(val mood: MoodUiModel) : EntryUiEvent
    data class SheetConfirmedClicked(val mood: MoodUiModel) : EntryUiEvent
    data class TitleChanged(val title: String) : EntryUiEvent
    data class TopicChanged(val topic: String) : EntryUiEvent
    data class DescriptionChanged(val description: String) : EntryUiEvent
}