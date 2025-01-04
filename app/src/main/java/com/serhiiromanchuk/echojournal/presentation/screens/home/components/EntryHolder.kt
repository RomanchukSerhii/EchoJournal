@file:OptIn(ExperimentalLayoutApi::class)

package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.presentation.core.components.ExpandableText
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodProvider
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.theme.EchoUltraLightGray
import com.serhiiromanchuk.echojournal.presentation.theme.NeutralVariant90
import com.serhiiromanchuk.echojournal.utils.InstantFormatter

@Composable
fun EntryHolder(
    entry: Entry,
    entryPosition: EntryListPosition,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        // Mood icon
        MoodTimeline(
            moodRes = MoodProvider.getMoodIcon(entry.moodType),
            entryPosition = entryPosition,
            modifier = Modifier.fillMaxHeight()
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 12.dp, bottom = 14.dp)
            ) {
                EntryHeader(
                    title = entry.title,
                    creationTime = InstantFormatter.formatHoursAndMinutes(entry.creationTimestamp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                EntryPlayer(
                    moodType = entry.moodType,
                    onPlayClick = {}
                )

                // Entry description
                if (entry.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    ExpandableText(
                        text = entry.description,
                        style = MaterialTheme.typography.bodyMedium,
                        clickableTextStyle = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Topic Chips
                if (entry.topics.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        entry.topics.forEach { topic ->
                            TopicChip(title = topic)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryHeader(
    modifier: Modifier = Modifier,
    title: String,
    creationTime: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = creationTime,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun EntryPlayer(
    modifier: Modifier = Modifier,
    moodType: MoodType,
    onPlayClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        color = MoodProvider.getMoodColor(moodType).background
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Play button
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onPlayClick() },
                shape = CircleShape,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play_button),
                    tint = MoodProvider.getMoodColor(moodType).button
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(MoodProvider.getMoodColor(moodType).track, CircleShape)
            )

            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = "0:00/12:30",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun TopicChip(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(22.dp)
            .clip(CircleShape)
            .background(
                color = EchoUltraLightGray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.5f
                        )
                    )
                ) {
                    append("# ")
                }
                append(title)
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MoodTimeline(
    @DrawableRes moodRes: Int,
    entryPosition: EntryListPosition,
    modifier: Modifier = Modifier,
    iconTopPadding: Dp = 16.dp,
    iconEndPadding: Dp = 12.dp
) {
    // State to store the height of the element (Box) for later use
    var elementHeight by remember { mutableIntStateOf(0) }

    // State to store the size of the mood image (icon)
    var moodSize by remember { mutableStateOf(IntSize.Zero) }

    // Derived state for the middle vertical offset of the mood icon, calculated based on its size and top padding
    val middleMoodOffsetY by remember(moodSize) {
        derivedStateOf {
            iconTopPadding.value.toInt() + moodSize.height / 2
        }
    }
    // Derived state for the horizontal offset (X position) of the divider, calculated from the mood icon width
    val dividerOffsetX by remember(moodSize) {
        derivedStateOf { moodSize.width / 2 }
    }

    // Derived state for the vertical offset (Y position) of the divider, adjusted based on the entry position
    val dividerOffsetY by remember(middleMoodOffsetY) {
        derivedStateOf {
            // For 'Last' or 'Middle' entries, the divider starts at the top; for 'First', it starts lower
            if (entryPosition == EntryListPosition.Last ||
                entryPosition == EntryListPosition.Middle
            ) 0 else middleMoodOffsetY
        }
    }

    // Derived state for the height of the vertical divider, calculated based on the entry position and element height
    val dividerHeight by remember(entryPosition, elementHeight, moodSize) {
        derivedStateOf {
            // The height is adjusted based on the entry's position in the list
            when (entryPosition) {
                EntryListPosition.First -> elementHeight - middleMoodOffsetY
                EntryListPosition.Last -> middleMoodOffsetY
                EntryListPosition.Middle -> elementHeight
                EntryListPosition.Single -> 0
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .onSizeChanged { elementHeight = it.height }
    ) {
        VerticalDivider(
            modifier = Modifier
                .offset {
                    IntOffset(dividerOffsetX, dividerOffsetY)
                }
                .height(dividerHeight.toDp()),
            color = NeutralVariant90
        )

        Image(
            modifier = Modifier
                .padding(top = iconTopPadding, end = iconEndPadding)
                .width(32.dp)
                .onSizeChanged { moodSize = it },
            painter = painterResource(moodRes),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
    }
}

@Composable
private fun Int.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) { toDp() }
}