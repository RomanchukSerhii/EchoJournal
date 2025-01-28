package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.FilterState

@Composable
fun EchoFilter(
    filterState: FilterState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Moods Chip
            item {
                FilterChip(
                    defaultTitle = stringResource(R.string.all_moods),
                    filterItems = filterState.moodFilterItems,
                    isFilterSelected = filterState.isMoodsOpen,
                    onClick = { onEvent(HomeUiEvent.MoodsFilterToggled) },
                    onClearClick = { onEvent(HomeUiEvent.MoodsFilterClearClicked) },
                    leadingIcon = {
                        if (filterState.moodFilterItems.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-4).dp)
                            ) {
                                filterState.moodFilterItems.forEach { filterItem ->
                                    if (filterItem.isChecked) {
                                        val mood = filterItem.title.toMoodUiModel()
                                        Image(
                                            modifier = Modifier.height(22.dp),
                                            painter = painterResource(mood.moodIcons.stroke),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }

            // Topic Chip
            item {
                FilterChip(
                    defaultTitle = stringResource(R.string.all_topics),
                    filterItems = filterState.topicFilterItems,
                    isFilterSelected = filterState.isTopicsOpen,
                    onClick = { onEvent(HomeUiEvent.TopicsFilterToggled) },
                    onClearClick = { onEvent(HomeUiEvent.TopicsFilterClearClicked) },
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    defaultTitle: String,
    filterItems: List<FilterState.FilterItem>,
    isFilterSelected: Boolean,
    onClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    AssistChip(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        colors = AssistChipDefaults.assistChipColors(
//            containerColor = if (isFilterSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
            containerColor = when {
                isFilterSelected -> MaterialTheme.colorScheme.surface
                filterItems.isNotEmpty() -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.background
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isFilterSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        label = {
            Text(
                text = getFormatFilterTitle(defaultTitle, filterItems),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        trailingIcon = {
            val isSomeMoodSelected = filterItems.any { it.isChecked }
            if (isSomeMoodSelected) {
                // Clear icon
                IconButton(
                    modifier = Modifier.size(18.dp),
                    onClick = { onClearClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_filter),
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon
    )
}

private fun getFormatFilterTitle(
    defaultTitle: String,
    filterItems: List<FilterState.FilterItem>
): String {
    val pickedItems = filterItems
        .filter { it.isChecked }
        .map { it.title }

    return when {
        pickedItems.isEmpty() -> defaultTitle
        pickedItems.size == 1 -> pickedItems.first()
        pickedItems.size == 2 -> pickedItems.joinToString(", ")
        else -> {
            val firstTwo = pickedItems.take(2).joinToString(", ")
            "$firstTwo +${pickedItems.size - 2}"
        }
    }
}