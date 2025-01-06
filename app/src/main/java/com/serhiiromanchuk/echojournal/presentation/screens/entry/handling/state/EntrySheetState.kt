package com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Excited
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Neutral
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Peaceful
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Sad
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Stressed

@Stable
data class EntrySheetState (
    val isOpen: Boolean = true,
    val activeMood: MoodUiModel = MoodUiModel.Undefined,
    val moods: List<MoodUiModel> = listOf(
        Stressed,
        Sad,
        Neutral,
        Peaceful,
        Excited
    )
)