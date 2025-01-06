package com.serhiiromanchuk.echojournal.presentation.screens.entry

import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.*
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntrySheetState
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntryUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

typealias BaseEntryViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

@HiltViewModel(assistedFactory = EntryViewModel.EntryViewModelFactory::class)
class EntryViewModel @AssistedInject constructor(
    @Assisted val entryFilePath: String,
    @Assisted val entryId: Long
) : BaseEntryViewModel() {
    override val initialState: EntryUiState
        get() = EntryUiState()

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
            is TitleChanged -> updateState { it.copy(title = event.title) }
            is TopicChanged -> updateState { it.copy(topic = event.topic) }
            is DescriptionChanged -> updateState { it.copy(description = event.description) }
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