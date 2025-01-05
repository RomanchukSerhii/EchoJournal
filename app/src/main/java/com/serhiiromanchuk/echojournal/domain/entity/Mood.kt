package com.serhiiromanchuk.echojournal.domain.entity

sealed class MoodType(val title: String = "") {
    data object Undefined : MoodType()
    data object Excited : MoodType("Excited")
    data object Peaceful : MoodType("Peaceful")
    data object Neutral : MoodType("Neutral")
    data object Sad : MoodType("Sad")
    data object Stressed : MoodType("Stressed")
}
