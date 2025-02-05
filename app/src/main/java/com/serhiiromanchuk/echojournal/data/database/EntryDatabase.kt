package com.serhiiromanchuk.echojournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serhiiromanchuk.echojournal.data.converter.InstantConverter
import com.serhiiromanchuk.echojournal.data.converter.MoodTypeConverter
import com.serhiiromanchuk.echojournal.data.converter.TopicsConverter
import com.serhiiromanchuk.echojournal.data.entity.EntryDb

// FEEDBACK: Usage of two databases in app
@Database(entities = [EntryDb::class], version = 1, exportSchema = false)
@TypeConverters(MoodTypeConverter::class, TopicsConverter::class, InstantConverter::class)
abstract class EntryDatabase : RoomDatabase() {
    abstract fun getEntryDao(): EntryDao
}