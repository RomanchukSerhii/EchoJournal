package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.handling.ActionEvent

sealed interface EntryActionEvent : ActionEvent {
    data object NavigateBack : EntryActionEvent
}