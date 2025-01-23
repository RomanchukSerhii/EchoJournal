package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.presentation.core.base.common.UiState
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import java.time.Instant

data class HomeUiState(
    val entries: Map<Instant, List<EntryHolderState>> = mapOf(),
    val filteredEntries: Map<Instant, List<EntryHolderState>> = mapOf(),
    val filterState: FilterState = FilterState(),
    val isFilterActive: Boolean = false,
    val homeSheetState: HomeSheetState = HomeSheetState(),
    val isPermissionDialogOpen: Boolean = false,
) : UiState {
//    val isFilterActive = filterState.moodFilterItems.isNotEmpty() || filterState.topicFilterItems.isNotEmpty()
    data class EntryHolderState(
        val entry: Entry
    ) {
        val playerState = PlayerState(
            duration = entry.audioDuration,
            amplitudeLogFilePath = entry.amplitudeLogFilePath
        )
    }
}
