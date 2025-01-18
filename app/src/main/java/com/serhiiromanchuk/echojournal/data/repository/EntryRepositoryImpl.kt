package com.serhiiromanchuk.echojournal.data.repository

import com.serhiiromanchuk.echojournal.data.database.EntryDao
import com.serhiiromanchuk.echojournal.data.mapper.toEntries
import com.serhiiromanchuk.echojournal.data.mapper.toEntryDb
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EntryRepositoryImpl @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {
    override fun getEntries(): Flow<List<Entry>> = entryDao.getEntries().map { it.toEntries() }

    override suspend fun upsertEntry(entry: Entry) = entryDao.upsertEntry(entry.toEntryDb())

    override suspend fun deleteEntry(entry: Entry) = entryDao.deleteEntry(entry.toEntryDb())
}