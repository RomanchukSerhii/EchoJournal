package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R

@Composable
fun TopicDropdown(
    searchQuery: String,
    topics: List<String>,
    onTopicClick: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    startOffset: IntOffset = IntOffset.Zero
) {
    Surface(
        modifier = modifier.offset { startOffset },
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 8.dp
    ) {
        LazyColumn (
            modifier = Modifier.padding(4.dp)
        ) { 
            items(items = topics) { topic ->
                TopicItem(
                    topic = topic,
                    onClick = { onTopicClick(topic) }
                )
            }
            item {
                CreateButton(
                    searchQuery = searchQuery,
                    onClick = { onCreateClick() }
                )
            }
        }
    }
}

@Composable
private fun TopicItem(
    topic: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "#",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )

        // Topic
        Text(
            text =  topic,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
private fun CreateButton(
    searchQuery: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.create_topic, searchQuery),
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Create ‘$searchQuery’",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}