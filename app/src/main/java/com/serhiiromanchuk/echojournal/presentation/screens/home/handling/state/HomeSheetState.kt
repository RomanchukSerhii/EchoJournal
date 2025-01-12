package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

data class HomeSheetState(
    val isVisible: Boolean = false,
    val isRecording: Boolean = true,
    val recordingTime: String = "00:00:00"
)