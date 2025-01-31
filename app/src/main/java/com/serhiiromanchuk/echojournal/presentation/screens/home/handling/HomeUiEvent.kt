package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiEvent

sealed interface HomeUiEvent : UiEvent {

    data object MoodsFilterToggled : HomeUiEvent
    data class MoodFilterItemClicked(val title: String) : HomeUiEvent
    data object MoodsFilterClearClicked : HomeUiEvent

    data object TopicsFilterToggled : HomeUiEvent
    data class TopicFilterItemClicked(val title: String) : HomeUiEvent
    data object TopicsFilterClearClicked : HomeUiEvent

    data class PermissionDialogOpened(val isOpen: Boolean) : HomeUiEvent

    data object StartRecording : HomeUiEvent
    data object PauseRecording : HomeUiEvent
    data object ResumeRecording : HomeUiEvent
    data class StopRecording(val saveFile: Boolean) : HomeUiEvent

    data object ActionButtonStartRecording : HomeUiEvent
    data class ActionButtonStopRecording(val saveFile: Boolean = true) : HomeUiEvent

    data class EntryPlayClick(val entryId: Long) : HomeUiEvent
    data class EntryPauseClick(val entryId: Long) : HomeUiEvent
    data class EntryResumeClick(val entryId: Long) : HomeUiEvent
}