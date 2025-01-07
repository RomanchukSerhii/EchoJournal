package com.serhiiromanchuk.echojournal.domain.entity

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.utils.Constants
import java.time.Instant

@Stable
data class Entry(
    val id: Long = Constants.INITIAL_ENTRY_ID,
    val title: String = "",
    val description: String = "",
    val moodType: MoodType = MoodType.Neutral,
    val topics: List<Topic> = listOf(),
    val creationTimestamp: Instant = Instant.now()
)
