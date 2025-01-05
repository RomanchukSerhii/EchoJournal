package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState

data class EntryUiState(
    val title: String = "",
    val topic: String = "",
    val description: String = ""
) : UiState {
}