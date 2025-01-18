package com.serhiiromanchuk.echojournal.data.repository

import com.serhiiromanchuk.echojournal.data.database.TopicDao
import com.serhiiromanchuk.echojournal.data.mapper.toTopic
import com.serhiiromanchuk.echojournal.data.mapper.toTopicDb
import com.serhiiromanchuk.echojournal.data.mapper.toTopics
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val topicDao: TopicDao
) : TopicRepository {
    override fun getTopics(): Flow<List<Topic>> = topicDao.getTopics().map { it.toTopics() }

    override suspend fun searchTopics(query: String): List<Topic> =
        topicDao.searchTopics(query).map { it.toTopic() }

    override suspend fun insertTopic(topic: Topic) = topicDao.insertTopic(topic.toTopicDb())

    override suspend fun deleteTopic(topic: Topic) = topicDao.deleteTopic(topic.toTopicDb())
}