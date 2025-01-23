package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel

@Stable
data class FilterState(
    val isMoodsOpen: Boolean = false,
    val isTopicsOpen: Boolean = false,
    val moodFilterItems: List<FilterItem> = MoodUiModel.allMoods.map { FilterItem(title = it.title) },
    val topicFilterItems: List<FilterItem> = listOf()
) {
    data class FilterItem(
        val title: String = "",
        val isChecked: Boolean = false
    )
}