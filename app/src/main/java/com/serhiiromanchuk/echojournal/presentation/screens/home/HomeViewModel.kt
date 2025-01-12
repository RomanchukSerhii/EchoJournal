package com.serhiiromanchuk.echojournal.presentation.screens.home

import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.BottomSheetToggled
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.CancelRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PauseRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PermissionDialogOpened
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.ResumeRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StartRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StopRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

private typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder
) : HomeBaseViewModel() {
    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId).atStartOfDay(zoneId).toInstant()
        val yesterday = today.minusSeconds(86400) // Мінус 1 день
        val dayBeforeYesterday = yesterday.minusSeconds(86400) // Мінус ще 1 день

        val topics = listOf(
            Topic(
                id = 0,
                name = "Work"
            ),
            Topic(
                id = 0,
                name = "Conundrums"
            ),
        )

        val entries = mapOf(
            Pair(
                today, listOf(
                    Entry(
                        id = 4L,
                        title = "MyEntry",
                        description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.",
                        creationTimestamp = today
                    ),
                    Entry(
                        id = 3L,
                        title = "MyEntry",
                        moodType = MoodType.Peaceful,
                        topics = topics,
                        creationTimestamp = today
                    )
                )
            ),
            Pair(
                yesterday, listOf(
                    Entry(
                        id = 2L,
                        title = "MyEntry",
                        moodType = MoodType.Sad,
                        creationTimestamp = yesterday
                    ),
                    Entry(
                        id = 1L,
                        title = "MyEntry",
                        moodType = MoodType.Excited,
                        description = "It is a long established fact that a reader will be distracted by the readable content of a page.",
                        creationTimestamp = yesterday
                    )
                )
            ),
            Pair(
                dayBeforeYesterday, listOf(
                    Entry(
                        id = 0L,
                        title = "MyEntry",
                        moodType = MoodType.Stressed,
                        creationTimestamp = dayBeforeYesterday
                    ),
                )
            )
        )
        updateState {
            it.copy(
                entries = entries
            )
        }
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            BottomSheetToggled -> toggleSheetState()
            StartRecording -> TODO()
            PauseRecording -> {
                audioRecorder.pause()
                toggleRecordingState()
            }
            ResumeRecording -> {
                audioRecorder.resume()
                toggleRecordingState()
            }
            StopRecording -> toggleSheetState()
            CancelRecording -> TODO()
            is PermissionDialogOpened -> updateState { it.copy(isPermissionDialogOpen = event.isOpen) }
        }
    }

    private fun pauseRecording() {
        audioRecorder.pause()
        toggleRecordingState()
    }

    private fun toggleSheetState() {
        val updateSheetState = currentState.homeSheetState.copy(isVisible = !currentState.homeSheetState.isVisible)

        if (updateSheetState.isVisible) {
            val outputFile = audioRecorder.createAudioFile()
            audioRecorder.start(outputFile)
        } else {
            audioRecorder.stop()
        }

        updateState { it.copy(homeSheetState = updateSheetState) }
    }

    private fun toggleRecordingState() {
        val updateSheetState = currentState.homeSheetState.copy(isRecording = !currentState.homeSheetState.isRecording)
        updateState { it.copy(homeSheetState = updateSheetState) }
    }
}