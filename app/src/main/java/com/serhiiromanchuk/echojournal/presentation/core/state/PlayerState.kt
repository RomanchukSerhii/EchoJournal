package com.serhiiromanchuk.echojournal.presentation.core.state

data class PlayerState(
    val duration: String = "",
    val currentPosition: String = "",
    val action: Action = Action.Initializing
) {
    sealed interface Action {
        data object Initializing : Action
        data object Playing : Action
        data object Paused : Action
        data object Resumed : Action
    }
}