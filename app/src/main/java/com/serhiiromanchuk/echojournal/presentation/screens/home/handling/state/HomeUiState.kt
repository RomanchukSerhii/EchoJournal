package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState

data class HomeUiState(
    val echos: List<String> = listOf(),
    val filterState: FilterState = FilterState()
) : UiState
