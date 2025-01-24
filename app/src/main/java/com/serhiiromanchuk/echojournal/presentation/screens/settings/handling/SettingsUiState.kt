package com.serhiiromanchuk.echojournal.presentation.screens.settings.handling

import androidx.compose.ui.unit.IntOffset
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

data class SettingsUiState(
    val activeMood: MoodUiModel = MoodUiModel.Undefined,
    val moods: List<MoodUiModel> = MoodUiModel.allMoods,
    val topicState: TopicState = TopicState()
) : UiState {

    data class TopicState(
        val topicValue: String = "",
        val currentTopics: List<Topic> = listOf(),
        val foundTopics: List<Topic> = listOf(),
        val topicDropdownOffset: IntOffset = IntOffset.Zero,
        val isAddButtonVisible: Boolean = true
    )
}
