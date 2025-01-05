package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

sealed interface BottomSheetState {
    data object Closed : BottomSheetState
    data class Recording(val recordingTime: String = "00:00:00") : BottomSheetState
    data class Pause(val recordingTime: String = "00:00:00") : BottomSheetState
}