package com.serhiiromanchuk.echojournal.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.EchoFAB
import com.serhiiromanchuk.echojournal.presentation.core.components.EchoTopBar
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EchoFilter
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EmptyHomeScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.JournalEntries
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.RecordingBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.BottomSheetState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {
    val viewModel: HomeViewModel = hiltViewModel()

    BaseContentLayout(
        modifier = modifier.padding(horizontal = 16.dp),
        viewModel = viewModel,
        topBar = {
            EchoTopBar(
                title = stringResource(R.string.your_echojournal),
                onSettingsClick = {}
            )
        },
        floatingActionButton = {
            EchoFAB(
                onClick = {
                    viewModel.onEvent(HomeUiEvent.BottomSheetStateChanged(BottomSheetState.Recording()))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { uiState ->
        if (uiState.entries.isEmpty()) {
            EmptyHomeScreen(modifier = Modifier.padding(16.dp))
        } else {
            HomeScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            RecordingBottomSheet(
                bottomSheetState = uiState.bottomSheetState,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column {
        EchoFilter(
            filterState = uiState.filterState,
            onEvent = onEvent
        )
        JournalEntries(
            entryNotes = uiState.entries,
            onEvent = onEvent
        )
    }
}