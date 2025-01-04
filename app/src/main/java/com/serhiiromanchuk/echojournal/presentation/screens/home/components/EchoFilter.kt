package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.FilterState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.FilterState.FilterItem

@Composable
fun EchoFilter(
    filterState: FilterState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Moods Chip
            item {
                FilterChip(
                    title = filterState.pickedMoodsString,
                    isSelected = filterState.isMoodsOpen,
                    onClick = {},
                    onClearClick = {},
                    leadingIcon = {
                        if (filterState.moodFilterItems.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-4).dp)
                            ) {
                                filterState.moodFilterItems.forEach { filterItem ->
                                    Image(
                                        modifier = Modifier.height(22.dp),
                                        painter = painterResource(filterItem.iconStrokeRes),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                )
            }

            // Topic Chip
            item {
                FilterChip(
                    title = filterState.pickedTopicsString,
                    isSelected = filterState.isTopicsOpen,
                    onClick = {},
                    onClearClick = {},
                )
            }
        }

        if (filterState.isMoodsOpen) {
            FilterList(
                filterItems = filterState.moodFilterItems,
                onDismissClicked = {}
            )
        }

        if (filterState.isTopicsOpen) {
            FilterList(
                filterItems = filterState.topicsFilterItems,
                onDismissClicked = {}
            )
        }
    }

}

@Composable
private fun FilterChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onClearClick: () -> Unit,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        trailingIcon = {
            if (isSelected) {
                IconButton(
                    modifier = Modifier.size(18.dp),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_filter),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        enabled = enabled,
        leadingIcon = leadingIcon,
        modifier = modifier
    )
}

@Composable
private fun FilterList(
    filterItems: List<FilterItem>,
    onDismissClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismissClicked() }
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filterItems, key = { it.iconRes }) { filterItem ->
                    FilterItem(filterItem)
                }
            }
        }
    }
}

@Composable
fun FilterItem(
    filterItem: FilterItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (filterItem.isChecked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(filterItem.iconRes),
                contentDescription = null,
                modifier = Modifier.height(24.dp)
            )

            Text(
                modifier = Modifier.weight(1f),
                text = filterItem.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            )

            if (filterItem.isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}