package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import com.serhiiromanchuk.echojournal.utils.Constants

data class HomeSheetState(
    val isVisible: Boolean = false,
    val isRecording: Boolean = true,
    val recordingTime: String = Constants.DEFAULT_FORMATTED_TIME
)