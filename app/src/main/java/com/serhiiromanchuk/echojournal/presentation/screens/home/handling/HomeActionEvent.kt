package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent {
    data class NavigateToEntryScreen(
        val audioFilePath: String,
        val amplitudeFilePath: String
    ) : HomeActionEvent
}