package com.serhiiromanchuk.echojournal.presentation.core.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.serhiiromanchuk.echojournal.R
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
import com.serhiiromanchuk.echojournal.utils.Constants

@Stable
sealed class MoodUiModel(
    val title: String,
    val moodIcon: MoodIcon,
    val moodColor: MoodColor,
) {
    data object Excited : MoodUiModel(
        title = "Excited",
        moodIcon = MoodIcon(
            fillIcon = R.drawable.ic_excited_mood,
            strokeIcon = R.drawable.ic_excited_stroke,
            outlineIcon = R.drawable.ic_excited_outline
        ),
        moodColor = MoodColor(
            button = MoodExcited35,
            track = MoodExcited80,
            background = MoodExcited95
        )
    )

    data object Neutral : MoodUiModel(
        title = "Neutral",
        moodIcon = MoodIcon(
            fillIcon = R.drawable.ic_neutral_mood,
            strokeIcon = R.drawable.ic_neutral_stroke,
            outlineIcon = R.drawable.ic_neutral_outline
        ),
        moodColor = MoodColor(
            button = MoodNeutral35,
            track = MoodNeutral80,
            background = MoodNeutral95
        )
    )

    data object Peaceful : MoodUiModel(
        title = "Peaceful",
        moodIcon = MoodIcon(
            fillIcon = R.drawable.ic_peaceful_mood,
            strokeIcon = R.drawable.ic_peaceful_stroke,
            outlineIcon = R.drawable.ic_peaceful_outline
        ),
        moodColor = MoodColor(
            button = MoodPeaceful35,
            track = MoodPeaceful80,
            background = MoodPeaceful95
        )
    )

    data object Sad : MoodUiModel(
        title = "Sad",
        moodIcon = MoodIcon(
            fillIcon = R.drawable.ic_sad_mood,
            strokeIcon = R.drawable.ic_sad_stroke,
            outlineIcon = R.drawable.ic_sad_outline
        ),
        moodColor = MoodColor(
            button = MoodSad35,
            track = MoodSad80,
            background = MoodSad95
        )
    )

    data object Stressed : MoodUiModel(
        title = "Stressed",
        moodIcon = MoodIcon(
            fillIcon = R.drawable.ic_stressed_mood,
            strokeIcon = R.drawable.ic_stressed_stroke,
            outlineIcon = R.drawable.ic_stressed_outline
        ),
        moodColor = MoodColor(
            button = MoodStressed35,
            track = MoodStressed80,
            background = MoodStressed95
        )
    )

    data object Undefined: MoodUiModel(
        title = "",
        moodIcon = MoodIcon(),
        moodColor = MoodColor()
    )
}

data class MoodColor(
    val button: Color = Color.Transparent,
    val track: Color = Color.Transparent,
    val background: Color = Color.Transparent
)

data class MoodIcon(
    val fillIcon: Int = Constants.UNDEFINED_MOOD_ICON_RES,
    val strokeIcon: Int = Constants.UNDEFINED_MOOD_ICON_RES,
    val outlineIcon: Int = Constants.UNDEFINED_MOOD_ICON_RES
)