package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.Topic

@Composable
fun TopicDropdown(
    searchQuery: String,
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    startOffset: IntOffset = IntOffset.Zero
) {
    var isVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(searchQuery) {
        isVisible = searchQuery.isNotEmpty()
    }

    if (isVisible) {
        Box(
            modifier = modifier
                .offset { startOffset }
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isVisible = false }
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 8.dp
            ) {
                LazyColumn (
                    modifier = Modifier.padding(4.dp)
                ) {
                    items(items = topics) { topic ->
                        TopicItem(
                            topic = topic.name,
                            onClick = {
                                onTopicClick(topic)
                                focusManager.clearFocus()
                            }
                        )
                    }
                    item {
                        CreateButton(
                            searchQuery = searchQuery,
                            onClick = {
                                onCreateClick()
                                isVisible = false
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
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
            .clip(RoundedCornerShape(6.dp))
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
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
//        Box(
//            modifier = Modifier.width(18.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = stringResource(R.string.create_topic, searchQuery),
//                modifier = Modifier.fillMaxSize(),
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }

        Image(
            painter = painterResource(R.drawable.ic_add_primary),
            contentDescription = stringResource(R.string.create_topic, searchQuery),
            modifier = Modifier.width(16.dp),
            contentScale = ContentScale.FillWidth
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