package com.serhiiromanchuk.echojournal.presentation.core.state

data class PlayerState(
    val duration: Int = 0,
    val durationText: String = "",
    val currentPosition: Int = 0,
    val currentPositionText: String = "",
    val action: Action = Action.Initializing,
    val amplitudeLogFilePath: String = ""
) {

    sealed interface Action {
        data object Initializing : Action
        data object Playing : Action
        data object Paused : Action
        data object Resumed : Action
    }
}