package com.serhiiromanchuk.echojournal.presentation.core.utils

import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.MoodType

object MoodProvider {
    fun getMoodIcon(type: MoodType): Int {
        return when (type) {
            MoodType.Excited -> R.drawable.ic_excited_mood
            MoodType.Neutral -> R.drawable.ic_neutral_mood
            MoodType.Peaceful -> R.drawable.ic_peaceful_mood
            MoodType.Sad -> R.drawable.ic_sad_mood
            MoodType.Stressed -> R.drawable.ic_stressed_mood
        }
    }
}