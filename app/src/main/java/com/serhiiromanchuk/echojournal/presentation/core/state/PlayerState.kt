package com.serhiiromanchuk.echojournal.presentation.core.state

data class PlayerState(
    val duration: String = "",
    val currentPosition: String = "",
    val action: Action = Action.Initializing,
    val trackDimensions: TrackDimensions = TrackDimensions()
) {

    sealed interface Action {
        data object Initializing : Action
        data object Playing : Action
        data object Paused : Action
        data object Resumed : Action
    }

    data class TrackDimensions(
        val trackWidth: Float = 0f,
        val amplitudeWidth: Float = 0f,
        val amplitudeSpacing: Float = 0f,
        val heightCoefficients: List<Float> = listOf()
    )
}