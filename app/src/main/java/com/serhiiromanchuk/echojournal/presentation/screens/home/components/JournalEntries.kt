@file:OptIn(ExperimentalFoundationApi::class)

package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.utils.InstantFormatter
import java.time.Instant

@Composable
fun JournalEntries(
    entryNotes: Map<Instant, List<Entry>>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 20.dp),
    ) {
        entryNotes.forEach { (instant, entries) ->
            item {
                // DataHeader
                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                    text = InstantFormatter.formatToRelativeDay(instant),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            items(items = entries, key = { entry -> entry.id }) { entry ->
                EntryHolder(
                    entry = entry,
                    entryPosition = when {
                        entries.size == 1 -> EntryListPosition.Single
                        entry == entries.first() -> EntryListPosition.First
                        entry == entries.last() -> EntryListPosition.Last
                        else -> EntryListPosition.Middle
                    },
                    onEvent = onEvent
                )
            }
        }
    }
}