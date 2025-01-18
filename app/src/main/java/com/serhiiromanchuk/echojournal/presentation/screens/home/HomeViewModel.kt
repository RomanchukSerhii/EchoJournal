package com.serhiiromanchuk.echojournal.presentation.screens.home

import android.net.Uri
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.CancelRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PauseRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PermissionDialogOpened
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.ResumeRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StartRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StopRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeSheetState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState
import com.serhiiromanchuk.echojournal.utils.StopWatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

private typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val audioRecorder: AudioRecorder
) : HomeBaseViewModel() {
    override val initialState: HomeUiState
        get() = HomeUiState()

    private val stopWatch = StopWatch()
    private var stopWatchJob: Job? = null

    init {
        launch {
            entryRepository.getEntries().collect { entries ->
                val sortedEntries = entries
                    .groupBy { entry ->
                        val localDate =
                            entry.creationTimestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                        localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                    }
                    .mapValues { (_, entryList) ->
                        entryList.map { HomeUiState.EntryHolderState(it) }
                    }
                updateState { it.copy(entries = sortedEntries) }
            }
        }
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            StartRecording -> startRecording()
            PauseRecording -> pauseRecording()
            ResumeRecording -> resumeRecording()
            is StopRecording -> stopRecording(event.saveFile)
            CancelRecording -> TODO()
            is PermissionDialogOpened -> updateState { it.copy(isPermissionDialogOpen = event.isOpen) }
        }
    }

    private fun startRecording() {
        toggleSheetState()
        audioRecorder.start()
        stopWatch.start()
        stopWatchJob = launch {
            stopWatch.formattedTime.collect {
                val updatedSheetState = currentState.homeSheetState.copy(recordingTime = it)
                updateHomeSheetState(updatedSheetState)
            }
        }
    }

    private fun pauseRecording() {
        audioRecorder.pause()
        stopWatch.pause()
        toggleRecordingState()
    }

    private fun resumeRecording() {
        audioRecorder.resume()
        stopWatch.start()
        toggleRecordingState()
    }

    private fun stopRecording(saveFile: Boolean) {
        val audioFilePath = audioRecorder.stop(saveFile)
        stopWatch.reset()
        stopWatchJob?.cancel()
        toggleSheetState()
        if (saveFile) {
            val amplitudeLogFilePath = audioRecorder.getAmplitudeLogFilePath()
            sendActionEvent(
                HomeActionEvent.NavigateToEntryScreen(
                    Uri.encode(audioFilePath), Uri.encode(amplitudeLogFilePath)
                )
            )
        }
    }

    private fun toggleSheetState() {
        val updatedSheetState =
            currentState.homeSheetState.copy(isVisible = !currentState.homeSheetState.isVisible)

        updateHomeSheetState(updatedSheetState)
    }

    private fun toggleRecordingState() {
        val updatedSheetState =
            currentState.homeSheetState.copy(isRecording = !currentState.homeSheetState.isRecording)
        updateHomeSheetState(updatedSheetState)
    }

    private fun updateHomeSheetState(updatedSheetState: HomeSheetState) {
        updateState { it.copy(homeSheetState = updatedSheetState) }
    }
}