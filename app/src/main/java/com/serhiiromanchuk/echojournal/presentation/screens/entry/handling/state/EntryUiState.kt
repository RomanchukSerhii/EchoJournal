package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

data class EntryUiState(
    val currentMood: MoodUiModel = MoodUiModel.Undefined,
    val title: String = "",
    val topic: String = "",
    val description: String = "",
    val entrySheetState: EntrySheetState = EntrySheetState()
) : UiState