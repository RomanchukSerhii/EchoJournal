package com.serhiiromanchuk.echojournal.presentation.core.state

import androidx.compose.runtime.Stable
import com.serhiiromanchuk.echojournal.utils.InstantFormatter

@Stable
data class PlayerState(
    val duration: Int = 0,
    val currentPosition: Int = 0,
    val currentPositionText: String = "00:00",
    val action: Action = Action.Initializing,
    val amplitudeLogFilePath: String = ""
) {
    val durationText = InstantFormatter.formatMillisToTime(duration.toLong())

    sealed interface Action {
        data object Initializing : Action
        data object Playing : Action
        data object Paused : Action
        data object Resumed : Action
    }
}