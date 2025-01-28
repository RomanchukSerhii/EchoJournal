package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiState
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import java.time.Instant

@Stable
data class HomeUiState(
    val entries: Map<Instant, List<EntryHolderState>> = mapOf(),
    val filteredEntries: Map<Instant, List<EntryHolderState>> = mapOf(),
    val filterState: FilterState = FilterState(),
    val isFilterActive: Boolean = false,
    val homeSheetState: HomeSheetState = HomeSheetState(),
    val isPermissionDialogOpen: Boolean = false
) : UiState {

    @Stable
    data class EntryHolderState(
        val entry: Entry,
        val playerState: PlayerState = PlayerState(
            duration = entry.audioDuration,
            amplitudeLogFilePath = entry.amplitudeLogFilePath
        )
    )
}
