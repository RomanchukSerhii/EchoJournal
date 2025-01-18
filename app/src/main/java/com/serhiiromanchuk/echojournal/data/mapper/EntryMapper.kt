package com.serhiiromanchuk.echojournal.data.mapper

import com.serhiiromanchuk.echojournal.data.entity.EntryDb
import com.serhiiromanchuk.echojournal.domain.entity.Entry

fun Entry.toEntryDb(): EntryDb = EntryDb(
    id = id,
    title = title,
    moodType = moodType,
    audioFilePath = audioFilePath,
    audioDuration = audioDuration,
    amplitudeLogFilePath = amplitudeLogFilePath,
    description = description,
    topics = topics,
    creationTimestamp = creationTimestamp
)

fun EntryDb.toEntry(): Entry = Entry(
    id = id,
    title = title,
    moodType = moodType,
    audioFilePath = audioFilePath,
    audioDuration = audioDuration,
    amplitudeLogFilePath = amplitudeLogFilePath,
    description = description,
    topics = topics,
    creationTimestamp = creationTimestamp
)

fun List<EntryDb>.toEntries(): List<Entry> = map { it.toEntry() }