package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState

data class HomeUiState(
    val echos: List<String> = listOf()
) : UiState
