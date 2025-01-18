package com.serhiiromanchuk.echojournal.data.converter

import androidx.room.TypeConverter
import com.serhiiromanchuk.echojournal.domain.entity.MoodType

class MoodTypeConverter {
    @TypeConverter
    fun fromMoodType(moodType: MoodType): String = moodType.title

    @TypeConverter
    fun toMoodType(value: String): MoodType {
        return when (value) {
            MoodType.Excited.title -> MoodType.Excited
            MoodType.Peaceful.title -> MoodType.Peaceful
            MoodType.Neutral.title -> MoodType.Neutral
            MoodType.Sad.title -> MoodType.Sad
            MoodType.Stressed.title -> MoodType.Stressed
            else -> MoodType.Undefined
        }
    }
}