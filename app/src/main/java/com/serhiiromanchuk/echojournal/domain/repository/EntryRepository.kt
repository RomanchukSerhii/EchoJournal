package com.serhiiromanchuk.echojournal.domain.repository

import com.serhiiromanchuk.echojournal.domain.entity.Entry
import kotlinx.coroutines.flow.Flow

interface EntryRepository {

    fun getEntries(): Flow<List<Entry>>

    suspend fun upsertEntry(entry: Entry)

    suspend fun deleteEntry(entry: Entry)
}