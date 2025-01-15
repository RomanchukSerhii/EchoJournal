package com.serhiiromanchuk.echojournal.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.navigation.Screen
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EchoFilter
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EmptyHomeScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeFAB
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeTopBar
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.JournalEntries
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.RecordingBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {
    val viewModel: HomeViewModel = hiltViewModel()

    BaseContentLayout(
        modifier = modifier,
        viewModel = viewModel,
        topBar = {
            HomeTopBar(
                title = stringResource(R.string.your_echojournal),
                onSettingsClick = {
                    navigationState.navigateTo(Screen.Settings.route)
                }
            )
        },
        floatingActionButton = {
            HomeFAB(
                onResult = { isGranted ->
                    if (isGranted) viewModel.onEvent(HomeUiEvent.BottomSheetToggled)
                }
            )
        },
        actionsEventHandler = { _, actionEvent ->
            when (actionEvent) {
                is HomeActionEvent.NavigateToEntryScreen ->
                    navigationState.navigateToEntry(
                        audioFilePath = actionEvent.audioFilePath,
                        amplitudeLogFilePath = actionEvent.amplitudeFilePath
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
        }
        RecordingBottomSheet(
            homeSheetState = uiState.homeSheetState,
            onEvent = viewModel::onEvent
        )
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