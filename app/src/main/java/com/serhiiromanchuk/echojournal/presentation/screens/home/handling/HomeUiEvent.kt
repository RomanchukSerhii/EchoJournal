package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent

sealed interface HomeUiEvent : UiEvent {

    data object MoodsFilterToggled : HomeUiEvent

    data class MoodFilterItemClicked(val title: String) : HomeUiEvent

    data object MoodsFilterClearClicked : HomeUiEvent

    data object TopicsFilterToggled : HomeUiEvent

    data class TopicFilterItemClicked(val title: String) : HomeUiEvent

    data object TopicsFilterClearClicked : HomeUiEvent

    data object StartRecording : HomeUiEvent

    data object PauseRecording : HomeUiEvent

    data object ResumeRecording : HomeUiEvent

    data class StopRecording(val saveFile: Boolean) : HomeUiEvent

    data object CancelRecording : HomeUiEvent

    data class PermissionDialogOpened(val isOpen: Boolean) : HomeUiEvent
}