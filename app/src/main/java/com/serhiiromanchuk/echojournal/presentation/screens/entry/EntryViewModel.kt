package com.serhiiromanchuk.echojournal.presentation.screens.entry

import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.domain.repository.TopicDbRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.BottomSheetClosed
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.BottomSheetOpened
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.DescriptionValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.MoodSelected
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.SheetConfirmedClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TitleValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TopicValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntrySheetState
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntryUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

typealias BaseEntryViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = EntryViewModel.EntryViewModelFactory::class)
class EntryViewModel @AssistedInject constructor(
    @Assisted val entryFilePath: String,
    @Assisted val entryId: Long,
    private val topicDbRepository: TopicDbRepository
) : BaseEntryViewModel() {
    override val initialState: EntryUiState
        get() = EntryUiState()

    private val searchQuery = MutableStateFlow("")
    private val searchResults: StateFlow<List<Topic>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                flow {
                    val test = topicDbRepository.searchTopics(query)
                    emit(test)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        launch {
            searchResults.collect{
                updateState { it.copy(foundTopics = searchResults.value) }
            }
        }
    }

    override fun onEvent(event: EntryUiEvent) {
        when (event) {
            BottomSheetClosed -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState))
            }
            is BottomSheetOpened -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState, event.mood))
            }
            is MoodSelected -> updateActiveMood(event.mood)
            is SheetConfirmedClicked -> setCurrentMood(event.mood)
            is TitleValueChanged -> updateState { it.copy(titleValue = event.value) }
            is TopicValueChanged -> updateTopic(event.value)
            is DescriptionValueChanged -> updateState { it.copy(descriptionValue = event.value) }
            EntryUiEvent.CreateTopicClicked -> addNewTopic()
            is EntryUiEvent.TopicClicked -> updateCurrentTopics(event.topic)
            is EntryUiEvent.TagClearClicked -> updateState {
                it.copy(currentTopics = currentState.currentTopics - event.topic)
            }
        }
    }

    private fun addNewTopic() {
        val newTopic = Topic(name = currentState.topicValue)
        updateCurrentTopics(newTopic)
        launch {
            topicDbRepository.insertTopic(newTopic)
        }
    }

    private fun updateCurrentTopics(newTopic: Topic) {
        updateState {
            it.copy(
                currentTopics = currentState.currentTopics + newTopic,
                topicValue = ""
            )
        }
    }


    private fun setCurrentMood(mood: MoodUiModel) {
        updateState {
            it.copy(
                currentMood = mood,
                entrySheetState = toggleSheetState(currentState.entrySheetState)
            )
        }
    }

    private fun updateActiveMood(mood: MoodUiModel) {
        updateState {
            it.copy(
                entrySheetState = currentState.entrySheetState.copy(activeMood = mood)
            )
        }
    }

    private fun updateTopic(topic: String) {
        updateState { it.copy(topicValue = topic) }
        searchQuery.value = topic
    }

    private fun toggleSheetState(
        state: EntrySheetState,
        activeMood: MoodUiModel = MoodUiModel.Undefined
    ): EntrySheetState {
        return state.copy(
            isOpen = !state.isOpen,
            activeMood = activeMood
        )
    }

    @AssistedFactory
    interface EntryViewModelFactory {
        fun create(entryFilePath: String, entryId: Long): EntryViewModel
    }
}