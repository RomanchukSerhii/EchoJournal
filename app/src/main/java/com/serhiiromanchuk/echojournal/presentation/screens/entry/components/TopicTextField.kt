@file:OptIn(ExperimentalLayoutApi::class)

package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.components.TopicTag
import com.serhiiromanchuk.echojournal.presentation.theme.Inter

@Composable
fun TopicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    topics: List<Topic>,
    onTagClearClick: (Topic) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier.size(16.dp).align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        topics.forEach { topic ->
            TopicTag(
                topic = topic,
                onClearClick = onTagClearClick
            )
        }

        EntryTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            hintText = stringResource(R.string.topic)
        )
    }
}