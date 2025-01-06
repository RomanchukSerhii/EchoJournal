@file:OptIn(ExperimentalMaterial3Api::class)

package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntrySheetState

@Composable
fun EntryBottomSheet(
    entrySheetState: EntrySheetState,
    onEvent: (EntryUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val isPrimaryButtonEnabled by remember(entrySheetState.activeMood) {
        derivedStateOf {
            entrySheetState.activeMood != MoodUiModel.Undefined
        }
    }

    if (entrySheetState.isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(EntryUiEvent.BottomSheetToggled) },
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {

                // Title
                Text(
                    text = stringResource(R.string.how_are_you_doing),
                    style = MaterialTheme.typography.titleMedium
                )

                // MoodsRow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    entrySheetState.moods.forEach { moodUiModel ->
                        MoodItem(
                            iconRes = if (entrySheetState.activeMood == moodUiModel) {
                                moodUiModel.moodIcon.fillIcon
                            } else {
                                moodUiModel.moodIcon.outlineIcon
                            },
                            title = moodUiModel.title,
                            onClick = {
                                onEvent(EntryUiEvent.MoodSelected(moodUiModel))
                            }
                        )
                    }
                }

                EntryBottomButtons(
                    primaryButtonText = stringResource(R.string.confirm),
                    onCancelClick = {},
                    onConfirmClick = {},
                    primaryButtonEnabled = isPrimaryButtonEnabled,
                    primaryLeadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (isPrimaryButtonEnabled) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MoodItem(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = title,
            modifier = Modifier.height(40.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
    }
}