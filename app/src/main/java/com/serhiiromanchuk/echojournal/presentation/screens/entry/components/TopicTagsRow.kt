@file:OptIn(ExperimentalLayoutApi::class)

package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.components.TopicTag
import com.serhiiromanchuk.echojournal.presentation.core.components.TopicTextField

@Composable
fun TopicTagsRow(
    value: String,
    onValueChange: (String) -> Unit,
    topics: List<Topic>,
    onTagClearClick: (Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        topics.forEach { topic ->
            TopicTag(
                topic = topic,
                onClearClick = onTagClearClick
            )
        }

        TopicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )
    }
}