package com.serhiiromanchuk.echojournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import java.time.Instant

@Entity(tableName = "entries")
data class EntryDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val moodType: MoodType,
    val audioFilePath: String,
    val audioDuration: Int,
    val amplitudeLogFilePath: String,
    val description: String,
    val topics: List<String>,
    val creationTimestamp: Instant
)