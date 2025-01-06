package com.serhiiromanchuk.echojournal.presentation.screens.entry

import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
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
            EntryUiEvent.BottomSheetClosed -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState))
            }

            is EntryUiEvent.BottomSheetOpened -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState, event.mood))
            }
            is EntryUiEvent.MoodSelected -> updateActiveMood(event.mood)
            is EntryUiEvent.SheetConfirmedClicked -> setCurrentMood(event.mood)
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