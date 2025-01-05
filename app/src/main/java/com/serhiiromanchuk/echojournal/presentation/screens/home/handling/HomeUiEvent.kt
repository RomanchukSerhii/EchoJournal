package com.serhiiromanchuk.echojournal.presentation.screens.home.handling

import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.BottomSheetState

interface HomeUiEvent : UiEvent {

    data class BottomSheetStateChanged(val bottomSheetState: BottomSheetState) : HomeUiEvent
}