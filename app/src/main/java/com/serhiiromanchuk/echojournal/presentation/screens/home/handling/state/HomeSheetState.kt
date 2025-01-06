package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

sealed interface HomeSheetState {
    data object Closed : HomeSheetState
    data class Recording(val recordingTime: String = "00:00:00") : HomeSheetState
    data class Pause(val recordingTime: String = "00:00:00") : HomeSheetState
}