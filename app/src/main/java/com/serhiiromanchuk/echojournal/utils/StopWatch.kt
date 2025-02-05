package com.serhiiromanchuk.echojournal.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StopWatch {

    private val _formattedTime = MutableStateFlow(Constants.DEFAULT_FORMATTED_TIME)
    val formattedTime: StateFlow<String> = _formattedTime

    // FEEDBACK: Inject scope for testability
    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isRunning = false

    private var timeMillis = 0L
    private var lastTimestamp = 0L

    fun start() {
        if (isRunning) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            isRunning = true
            while (isRunning) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                _formattedTime.value = InstantFormatter.formatMillisToTime(timeMillis)
            }
        }
    }

    fun pause() {
        isRunning = false
    }

    fun reset() {
        coroutineScope.cancel()
        // FEEDBACK: Redundant initialisation of scope
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimestamp = 0L
        _formattedTime.value = Constants.DEFAULT_FORMATTED_TIME
        isRunning = false
    }
}