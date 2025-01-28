package com.serhiiromanchuk.echojournal.presentation.core.utils

import com.serhiiromanchuk.echojournal.domain.entity.MoodType

fun MoodType.toUiModel(): MoodUiModel {
    return when (this) {
        MoodType.Excited -> MoodUiModel.Excited
        MoodType.Neutral -> MoodUiModel.Neutral
        MoodType.Peaceful -> MoodUiModel.Peaceful
        MoodType.Sad -> MoodUiModel.Sad
        MoodType.Stressed -> MoodUiModel.Stressed
        MoodType.Undefined -> MoodUiModel.Undefined
    }
}

fun MoodUiModel.toMoodType(): MoodType {
    return when (this) {
        MoodUiModel.Excited -> MoodType.Excited
        MoodUiModel.Neutral -> MoodType.Neutral
        MoodUiModel.Peaceful -> MoodType.Peaceful
        MoodUiModel.Sad -> MoodType.Sad
        MoodUiModel.Stressed -> MoodType.Stressed
        MoodUiModel.Undefined -> MoodType.Undefined
    }
}