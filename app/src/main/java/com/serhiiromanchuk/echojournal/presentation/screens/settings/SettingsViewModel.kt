package com.serhiiromanchuk.echojournal.presentation.screens.settings

import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.domain.repository.SettingsRepository
import com.serhiiromanchuk.echojournal.domain.repository.TopicRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiEvent.*
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiState
import com.serhiiromanchuk.echojournal.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

typealias SettingsBaseViewModel = BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val settingsRepository: SettingsRepository
) : SettingsBaseViewModel() {
    override val initialState: SettingsUiState
        get() = SettingsUiState()

    private val defaultMood = settingsRepository.getMood(Constants.KEY_MOOD_SETTINGS)
    private val defaultTopicsId = settingsRepository.getTopics(Constants.KEY_TOPIC_SETTINGS)

    private val searchQuery = MutableStateFlow("")
    private val searchResults: StateFlow<List<Topic>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                flow {
                    val foundTopics = topicRepository.searchTopics(query)
                    emit(foundTopics)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        // Set default settings
        launch {
            val defaultTopics = topicRepository.getTopicsByIdList(defaultTopicsId)
            updateState {
                it.copy(
                    activeMood = defaultMood.toMoodUiModel(),
                    topicState = currentState.topicState.copy(currentTopics = defaultTopics),
                )
            }
        }

        // Subscribe to topic search results
        launch {
            searchResults.collect {
                updateTopicState {
                    it.copy(foundTopics = searchResults.value)
                }
            }
        }
    }

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is TopicValueChanged -> updateTopicValue(event.value)
            is TagClearClicked -> clearTopic(event.topic)
            is TopicClicked -> updateCurrentTopics(event.topic)
            CreateTopicClicked -> addNewTopic()
            AddButtonVisibleToggled -> toggleAddButtonVisibility()
            is MoodSelected -> selectMood(event.mood)
        }
    }

    private fun selectMood(mood: MoodUiModel) {
        val updatedMood = if (currentState.activeMood == mood) MoodUiModel.Undefined else mood
        updateState {
            it.copy(activeMood = updatedMood)
        }
        if (!currentState.topicState.isAddButtonVisible) {
            updateTopicState { it.copy(isAddButtonVisible = true) }
        }
        settingsRepository.saveMood(Constants.KEY_MOOD_SETTINGS, updatedMood.title)
    }

    private fun clearTopic(topic: Topic) {
        updateTopicState {
            it.copy(currentTopics = currentState.topicState.currentTopics - topic)
        }
        saveTopicsSettings()
    }

    private fun toggleAddButtonVisibility() {
        updateTopicState {
            it.copy(isAddButtonVisible = !currentState.topicState.isAddButtonVisible)
        }
    }

    private fun updateTopicValue(topic: String) {
        updateTopicState { it.copy(topicValue = topic) }
        searchQuery.value = topic
    }

    private fun updateCurrentTopics(newTopic: Topic) {
        updateTopicState {
            it.copy(
                currentTopics = it.currentTopics + newTopic,
                topicValue = "",
                isAddButtonVisible = true
            )
        }
        saveTopicsSettings()
    }

    private fun addNewTopic() {
        val newTopic = Topic(name = currentState.topicState.topicValue)
        updateCurrentTopics(newTopic)
        saveTopicsSettings()
        launch {
            topicRepository.insertTopic(newTopic)
        }
    }

    private fun saveTopicsSettings() {
        val topicsId = currentState.topicState.currentTopics.map { it.id }
        settingsRepository.saveTopics(Constants.KEY_TOPIC_SETTINGS, topicsId)
    }

    private fun updateTopicState(update: (SettingsUiState.TopicState) -> SettingsUiState.TopicState) {
        updateState { it.copy(topicState = update(it.topicState)) }
    }
}