package com.serhiiromanchuk.echojournal.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.AppTopBar
import com.serhiiromanchuk.echojournal.presentation.core.components.MoodsRow
import com.serhiiromanchuk.echojournal.presentation.core.components.TopicDropdown
import com.serhiiromanchuk.echojournal.presentation.core.utils.toInt
import com.serhiiromanchuk.echojournal.presentation.screens.settings.components.SettingItem
import com.serhiiromanchuk.echojournal.presentation.screens.settings.components.TopicTagsWithAddButton
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
                onMoodClick = { onEvent(SettingsUiEvent.MoodSelected(it)) }
            )
        }

        // Topics setting
        Box {
            var topicOffset by remember { mutableStateOf(IntOffset.Zero) }

            // Will be used to calculate the y-axis offset of the topicOffset
            val verticalSpace = 16.dp.toInt()

            SettingItem(
                title = stringResource(R.string.my_topics),
                description = stringResource(R.string.topic_header_description)
            ) {
                TopicTagsWithAddButton(
                    topicState = uiState.topicState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            topicOffset = IntOffset(
                                coordinates.positionInParent().x.toInt(),
                                coordinates.positionInParent().y.toInt() + coordinates.size.height + verticalSpace
                            )
                        },
                )
            }

            TopicDropdown(
                searchQuery = uiState.topicState.topicValue,
                topics = uiState.topicState.foundTopics,
                onTopicClick = { onEvent(SettingsUiEvent.TopicClicked(it)) },
                onCreateClick = { onEvent(SettingsUiEvent.CreateTopicClicked) },
                startOffset = topicOffset,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }
    }
}