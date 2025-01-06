package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeSheetState

sealed interface HomeUiEvent : UiEvent {

    data class BottomSheetStateChanged(val homeSheetState: HomeSheetState) : HomeUiEvent
}