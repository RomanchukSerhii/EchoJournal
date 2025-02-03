package com.serhiiromanchuk.echojournal.presentation.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.ActionEvent
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiEvent
import com.serhiiromanchuk.echojournal.presentation.core.base.handling.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// FEEDBACK: Be careful of over-engineering. A Base class is rarely needed and
// couples all ViewModels together. A Change in the BaseViewModel will affect all ViewModels
// Introduces addition layers to click through which, for an app of this scale, makes it harder to navigate
abstract class BaseViewModel<S : UiState, E : UiEvent, A : ActionEvent> : ViewModel() {
    protected abstract val initialState: S

    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _actionEvent = Channel<A>()
    val actionEvent = _actionEvent.receiveAsFlow()

    protected val currentState: S
        get() = uiState.value

    protected fun updateState(block: (currentState: S) -> S) {
        _uiState.update(block)
    }

    protected fun sendActionEvent(actionEvent: A) {
        viewModelScope.launch { _actionEvent.send(actionEvent) }
    }

    // FEEDBACK: Using this inside a VM would be confusing since the dev would
    // need to click in here to ths scope used is the viewModelScope
    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = context, block = block)

    abstract fun onEvent(event: E)
}