package com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state

import androidx.compose.runtime.Stable

@Stable
data class FilterState(
    val isMoodsOpen: Boolean = false,
    val isTopicsOpen: Boolean = false,
    val moodFilterItems: List<FilterItem> = listOf(),
    val topicsFilterItems: List<FilterItem> = listOf()
) {
    val pickedMoodsString = getFormatStringList("All Moods", moodFilterItems.map { it.title })
    val pickedTopicsString = getFormatStringList("All Topics", topicsFilterItems.map { it.title })

    private fun getFormatStringList(defaultString: String, strings: List<String>): String {
        return when {
            strings.isEmpty() -> defaultString
            strings.size == 1 -> strings.first()
            strings.size == 2 -> strings.joinToString(", ")
            else -> {
                val firstTwo = strings.take(2).joinToString(", ")
                "$firstTwo +${strings.size - 2}"
            }
        }
    }

    data class FilterItem(
        val iconRes: Int,
        val iconStrokeRes: Int,
        val title: String,
        val isChecked: Boolean
    )
}