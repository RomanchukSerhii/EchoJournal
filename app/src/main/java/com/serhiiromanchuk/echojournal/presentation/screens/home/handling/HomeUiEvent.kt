package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent

sealed interface HomeUiEvent : UiEvent {

    data object BottomSheetToggled : HomeUiEvent

    data object StartRecording : HomeUiEvent

    data object PauseRecording : HomeUiEvent

    data object ResumeRecording : HomeUiEvent

    data object StopRecording : HomeUiEvent

    data object CancelRecording : HomeUiEvent

    data class PermissionDialogOpened(val isOpen: Boolean) : HomeUiEvent
}