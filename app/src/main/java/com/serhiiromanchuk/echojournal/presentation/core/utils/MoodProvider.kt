package com.serhiiromanchuk.echojournal.presentation.core.utils

import androidx.compose.ui.graphics.Color
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.presentation.theme.MoodExcited35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodExcited80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodExcited95
import com.serhiiromanchuk.echojournal.presentation.theme.MoodNeutral35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodNeutral80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodNeutral95
import com.serhiiromanchuk.echojournal.presentation.theme.MoodPeaceful35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodPeaceful80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodPeaceful95
import com.serhiiromanchuk.echojournal.presentation.theme.MoodSad35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodSad80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodSad95
import com.serhiiromanchuk.echojournal.presentation.theme.MoodStressed35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodStressed80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodStressed95
import com.serhiiromanchuk.echojournal.presentation.theme.MoodUndefined35
import com.serhiiromanchuk.echojournal.presentation.theme.MoodUndefined80
import com.serhiiromanchuk.echojournal.presentation.theme.MoodUndefined95
import com.serhiiromanchuk.echojournal.utils.Constants

data class MoodColor(
    val button: Color,
    val track: Color,
    val background: Color
)

object MoodProvider {
    fun getMoodIcon(type: MoodType): Int {
        return when (type) {
            MoodType.Excited -> R.drawable.ic_excited_mood
            MoodType.Neutral -> R.drawable.ic_neutral_mood
            MoodType.Peaceful -> R.drawable.ic_peaceful_mood
            MoodType.Sad -> R.drawable.ic_sad_mood
            MoodType.Stressed -> R.drawable.ic_stressed_mood
            MoodType.Undefined -> Constants.UNDEFINED_MOOD_TYPE_ICON
        }
    }

    fun getMoodColor(type: MoodType): MoodColor {
        return when (type) {
            MoodType.Excited -> MoodColor(
                button = MoodExcited35,
                track = MoodExcited80,
                background = MoodExcited95
            )
            MoodType.Neutral -> MoodColor(
                button = MoodNeutral35,
                track = MoodNeutral80,
                background = MoodNeutral95
            )
            MoodType.Peaceful -> MoodColor(
                button = MoodPeaceful35,
                track = MoodPeaceful80,
                background = MoodPeaceful95
            )
            MoodType.Sad -> MoodColor(
                button = MoodSad35,
                track = MoodSad80,
                background = MoodSad95
            )
            MoodType.Stressed -> MoodColor(
                button = MoodStressed35,
                track = MoodStressed80,
                background = MoodStressed95
            )

            MoodType.Undefined -> MoodColor(
                button = MoodUndefined35,
                track = MoodUndefined80,
                background = MoodUndefined95
            )
        }
    }
}