package com.serhiiromanchuk.echojournal.presentation.screens.settings

import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

typealias SettingsBaseViewModel = BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsActionEvent>

@HiltViewModel
class SettingsViewModel @Inject constructor() : SettingsBaseViewModel() {
    override val initialState: SettingsUiState
        get() = SettingsUiState()

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {

            else -> {}
        }
    }
}