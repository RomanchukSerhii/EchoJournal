package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import java.time.Instant

data class HomeUiState(
    val entries: Map<Instant, List<Entry>> = mapOf(),
    val filterState: FilterState = FilterState(),
    val homeSheetState: HomeSheetState = HomeSheetState(),
    val isPermissionDialogOpen: Boolean = false
) : UiState
