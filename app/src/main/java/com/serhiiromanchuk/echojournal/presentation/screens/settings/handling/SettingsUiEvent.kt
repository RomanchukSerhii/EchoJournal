package com.serhiiromanchuk.echojournal.presentation.screens.settings.handling

import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

sealed interface SettingsUiEvent : UiEvent {
    data class TopicValueChanged(val value: String) : SettingsUiEvent
    data class TopicClicked(val topic: Topic) : SettingsUiEvent
    data class TagClearClicked(val topic: Topic) : SettingsUiEvent
    data object CreateTopicClicked : SettingsUiEvent
    data object AddButtonVisibleToggled : SettingsUiEvent
    data class MoodSelected(val mood: MoodUiModel) : SettingsUiEvent
}