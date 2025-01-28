package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Stable
data class EntrySheetState (
    val isOpen: Boolean = true,
    val activeMood: MoodUiModel = MoodUiModel.Undefined,
    val moods: List<MoodUiModel> = MoodUiModel.allMoods
)