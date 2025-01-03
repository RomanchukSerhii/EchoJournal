package com.serhiiromanchuk.echojournal.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtils {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatHoursAndMinutes(instant: Instant, zoneId: ZoneId = ZoneId.systemDefault()): String {
        return instant.atZone(zoneId).toLocalDateTime().format(timeFormatter)
    }
}