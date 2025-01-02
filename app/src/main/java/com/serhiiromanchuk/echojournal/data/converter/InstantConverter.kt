package com.serhiiromanchuk.echojournal.data.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    @TypeConverter
    fun fromTimestamp(value: Long) = Instant.ofEpochMilli(value)


    @TypeConverter
    fun instantToTimestamp(instant: Instant) = instant.toEpochMilli()
}