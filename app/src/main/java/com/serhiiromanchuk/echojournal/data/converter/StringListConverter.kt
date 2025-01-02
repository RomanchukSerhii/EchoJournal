package com.serhiiromanchuk.echojournal.data.converter

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString(",")
    }
}