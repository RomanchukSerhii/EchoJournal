package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.FilterState.FilterItem

@Composable
fun FilterList(
    filterItems: List<FilterItem>,
    onItemClick: (String) -> Unit,
    onDismissClicked: () -> Unit,
    modifier: Modifier = Modifier,
    startOffset: IntOffset = IntOffset.Zero
) {
    Box(
        modifier = modifier
            .offset { startOffset }
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismissClicked() }
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filterItems) { filterItem ->
                    FilterItem(
                        filterItem = filterItem,
                        onClick = { onItemClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterItem(
    filterItem: FilterItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onClick(filterItem.title) },
        shape = RoundedCornerShape(8.dp),
        color = if (filterItem.isChecked) {
            MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.05f)
        } else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val moodUiModel = filterItem.title.toMoodUiModel()

            Image(
                painter = if (moodUiModel == MoodUiModel.Undefined) {
                    painterResource(R.drawable.ic_hashtag)
                } else {
                    painterResource(moodUiModel.moodIcons.fill)
                },
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