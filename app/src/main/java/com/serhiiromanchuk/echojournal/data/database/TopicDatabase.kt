package com.serhiiromanchuk.echojournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serhiiromanchuk.echojournal.data.entity.TopicDb

@Database(entities = [TopicDb::class], version = 1, exportSchema = false)
abstract class TopicDatabase : RoomDatabase() {
    abstract fun getTopicDao(): TopicDao
}