package com.serhiiromanchuk.echojournal.presentation.screens.settings.handling

import androidx.compose.ui.unit.IntOffset
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Excited
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Neutral
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Peaceful
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Sad
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel.Stressed

data class SettingsUiState(
    val activeMood: MoodUiModel = MoodUiModel.Undefined,
    val topicValue: String = "",
    val moods: List<MoodUiModel> = listOf(
        Stressed,
        Sad,
        Neutral,
        Peaceful,
        Excited
    ),
    val currentTopics: List<Topic> = listOf(),
    val foundTopics: List<Topic> = listOf(),
    val topicDropdownOffset: IntOffset = IntOffset.Zero
) : UiState
