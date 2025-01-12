package com.serhiiromanchuk.echojournal.utils

import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object InstantFormatter {

    private val zoneId: ZoneId = ZoneId.systemDefault()
    private val englishLocal: Locale = Locale("en", "US")

    fun formatToRelativeDay(instant: Instant): String {
        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)

        return when (val date = instant.atZone(zoneId).toLocalDate()) {
            today -> "TODAY"
            yesterday -> "YESTERDAY"
            else -> date.format(DateTimeFormatter.ofPattern("EEEE, MMM d", englishLocal))
        }
    }

    fun formatHoursAndMinutes(instant: Instant): String {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return instant.atZone(zoneId).toLocalDateTime().format(timeFormatter)
    }

    fun formatMillisToTime(timeMillis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            zoneId
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss:SS",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }
}