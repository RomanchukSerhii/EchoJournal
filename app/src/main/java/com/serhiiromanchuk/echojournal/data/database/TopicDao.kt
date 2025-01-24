package com.serhiiromanchuk.echojournal.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.serhiiromanchuk.echojournal.data.entity.TopicDb
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics ORDER BY name ASC")
    fun getTopics(): Flow<List<TopicDb>>

    @Query("SELECT * FROM topics WHERE id IN (:topicIds)")
    suspend fun getTopicsById(topicIds: List<Long>): List<TopicDb>

    @Query("SELECT * FROM topics WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchTopics(query: String): List<TopicDb>

    @Insert
    suspend fun insertTopic(topic: TopicDb)

    @Delete
    suspend fun deleteTopic(topic: TopicDb)
}