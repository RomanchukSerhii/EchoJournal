package com.serhiiromanchuk.echojournal.presentation.screens.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.AppTopBar
import com.serhiiromanchuk.echojournal.presentation.core.components.MoodPlayer
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryBottomButtons
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.EntryTextField
import com.serhiiromanchuk.echojournal.presentation.screens.entry.components.MoodChooseButton
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntryUiState
import com.serhiiromanchuk.echojournal.presentation.theme.Inter

@Composable
fun EntryScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    entryFilePath: String,
    entryId: Long
) {
    val viewModel: EntryViewModel =
        hiltViewModel<EntryViewModel, EntryViewModel.EntryViewModelFactory> { factory ->
            factory.create(entryFilePath, entryId)
        }

    BaseContentLayout(
        modifier = modifier,
        viewModel = viewModel,
        actionsEventHandler = { context, actionEvent ->

        },
        topBar = {
            AppTopBar(
                title = if (entryId < 0) {
                    stringResource(R.string.new_entry)
                } else {
                    stringResource(R.string.edit_entry)
                },
                onBackClick = { navigationState.popBackStack() }
            )
        },
        bottomBar = { uiState ->
            EntryBottomButtons(
                primaryButtonText = stringResource(R.string.save),
                onCancelClick = {},
                onConfirmClick = {},
                modifier = Modifier.padding(16.dp),
                primaryButtonEnabled = false
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { uiState ->
        EntryScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
        EntryBottomSheet(
            entrySheetState = uiState.entrySheetState,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun EntryScreen(
    uiState: EntryUiState,
    onEvent: (EntryUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // AddTitle text field
        EntryTextField(
            value = uiState.title,
            onValueChange = {},
            hintText = stringResource(R.string.add_title),
            leadingIcon = {
                MoodChooseButton(
                    mood = uiState.currentMood,
                    onClick = { onEvent(EntryUiEvent.BottomSheetOpened(uiState.currentMood)) }
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,
            iconSpacing = 6.dp
        )

        MoodPlayer(
            moodColor = uiState.currentMood.moodColor,
            onPlayClick = {},
            modifier = Modifier.height(44.dp),
        )

        // Topic text field
        EntryTextField(
            value = uiState.topic,
            onValueChange = {},
            hintText = stringResource(R.string.topic),
            leadingIcon = {
                Box(
                    modifier = Modifier.size(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "#",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        )

        // AddDescription field
        EntryTextField(
            value = uiState.description,
            onValueChange = {},
            hintText = stringResource(R.string.add_description),
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outlineVariant
                )
            }
        )
    }
}