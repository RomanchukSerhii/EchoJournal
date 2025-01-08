package com.serhiiromanchuk.echojournal.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.AppTopBar
import com.serhiiromanchuk.echojournal.presentation.core.components.MoodsRow
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.TopicDropdown
import com.serhiiromanchuk.echojournal.presentation.screens.settings.components.SettingItem
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.settings.handling.SettingsUiState

@Composable
fun SettingsScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {
    val viewModel: SettingsViewModel = hiltViewModel()

    BaseContentLayout(
        viewModel = viewModel,
        modifier = modifier.padding(top = 8.dp),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.settings),
                onBackClick = { navigationState.popBackStack() }
            )
        }
    ) { uiState ->
        SettingsScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mood setting
        SettingItem(
            title = stringResource(R.string.my_mood),
            description = stringResource(R.string.mood_header_description)
        ) {
            MoodsRow(
                moods = uiState.moods,
                activeMood = uiState.activeMood,
                onMoodClick = {}
            )
        }

        // Topics setting
        SettingItem(
            title = stringResource(R.string.my_topics),
            description = stringResource(R.string.topic_header_description)
        ) {
            TopicDropdown(
                searchQuery = uiState.topicValue,
                topics = uiState.foundTopics,
                onTopicClick = { },
                onCreateClick = { },
                startOffset = uiState.topicDropdownOffset
            )
        }
    }
}