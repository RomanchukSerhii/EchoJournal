package com.serhiiromanchuk.echojournal.domain.repository

import com.serhiiromanchuk.echojournal.domain.entity.MoodType

interface SettingsRepository {
    fun saveMood(key: String, moodTitle: String)
    fun getMood(key: String, defaultValue: String = MoodType.Undefined.title): String

    fun saveTopics(key: String, topicListId: List<Long>)
    fun getTopics(key: String): List<Long>
}