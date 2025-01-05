package com.serhiiromanchuk.echojournal.presentation.screens.entry

import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiState
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
        when(event) {

            else -> {}
        }
    }

    @AssistedFactory
    interface EntryViewModelFactory {
        fun create(entryFilePath: String, entryId: Long): EntryViewModel
    }
}