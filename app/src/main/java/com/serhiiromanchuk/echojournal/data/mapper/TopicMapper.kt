package com.serhiiromanchuk.echojournal.data.mapper

import com.serhiiromanchuk.echojournal.data.entity.TopicDb
import com.serhiiromanchuk.echojournal.domain.entity.Topic

fun Topic.toTopicDb(): TopicDb = TopicDb(
    id = id,
    name = name
)

fun TopicDb.toTopic(): Topic = Topic(
    id = id,
    name = name
)

fun List<TopicDb>.toTopics(): List<Topic> = map { it.toTopic() }