package com.serhiiromanchuk.echojournal.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.navigation.Screen
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EchoFilter
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EmptyHomeScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.FilterList
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeFAB
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeTopBar
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.JournalEntries
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.RecordingBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiState

@Composable
fun HomeScreenRoot(
    navigationState: NavigationState,
    isDataLoaded: () -> Unit,
    isLaunchedFromWidget: Boolean,
    modifier: Modifier = Modifier
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
                onResult = { isGranted, isLongClicked ->
                    if (isGranted) {
                        if (isLongClicked) {
                            viewModel.onEvent(HomeUiEvent.ActionButtonStartRecording)
                        } else {
                            viewModel.onEvent(HomeUiEvent.StartRecording)
                        }
                    }
                },
                onLongPressRelease = { isEntryCanceled ->
                    viewModel.onEvent(HomeUiEvent.ActionButtonStopRecording(saveFile = !isEntryCanceled))
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

                HomeActionEvent.DataLoaded -> {
                    isDataLoaded()
                    if (isLaunchedFromWidget) viewModel.onEvent(HomeUiEvent.StartRecording)
                }
            }
        }
    ) { uiState ->
        if (uiState.entries.isEmpty()  && !uiState.isFilterActive) {
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
    var filterOffset by remember { mutableStateOf(IntOffset.Zero) }

    Column {
        EchoFilter(
            filterState = uiState.filterState,
            onEvent = onEvent,
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    filterOffset = IntOffset(
                        coordinates.positionInParent().x.toInt(),
                        coordinates.positionInParent().y.toInt() + coordinates.size.height
                    )
                }
        )

        if (uiState.entries.isEmpty() && uiState.isFilterActive) {
            EmptyHomeScreen(
                title = stringResource(R.string.no_entries_found),
                supportingText = stringResource(R.string.no_entries_found_supporting_text)
            )
        }

        JournalEntries(
            entryNotes = uiState.entries,
            onEvent = onEvent
        )
    }

    if (uiState.filterState.isMoodsOpen) {
        FilterList(
            filterItems = uiState.filterState.moodFilterItems,
            onItemClick = { onEvent(HomeUiEvent.MoodFilterItemClicked(it)) },
            onDismissClicked = { onEvent(HomeUiEvent.MoodsFilterToggled) },
            startOffset = filterOffset
        )
    }

    if (uiState.filterState.isTopicsOpen) {
        FilterList(
            filterItems = uiState.filterState.topicFilterItems,
            onItemClick = { onEvent(HomeUiEvent.TopicFilterItemClicked(it)) },
            onDismissClicked = { onEvent(HomeUiEvent.TopicsFilterToggled) },
            startOffset = filterOffset
        )
    }
}