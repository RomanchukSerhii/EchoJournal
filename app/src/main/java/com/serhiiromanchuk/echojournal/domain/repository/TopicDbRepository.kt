package com.serhiiromanchuk.echojournal.domain.repository

import com.serhiiromanchuk.echojournal.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

interface TopicDbRepository {
    fun getTopics(): Flow<List<Topic>>

    suspend fun searchTopics(query: String): List<Topic>

    suspend fun insertTopic(topic: Topic)

    suspend fun deleteTopic(topic: Topic)
}