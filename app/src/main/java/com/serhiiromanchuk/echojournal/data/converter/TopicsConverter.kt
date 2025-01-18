package com.serhiiromanchuk.echojournal.data.converter

import androidx.room.TypeConverter

class TopicsConverter {
    @TypeConverter
    fun fromTopicsList(topics: List<String>): String {
        return topics.joinToString(",")
    }

    @TypeConverter
    fun toTopicsList(data: String): List<String> {
        return data.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
}