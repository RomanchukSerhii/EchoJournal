package com.serhiiromanchuk.echojournal.presentation.screens.entry

import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.echojournal.domain.audio.AudioPlayer
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import com.serhiiromanchuk.echojournal.domain.repository.SettingsRepository
import com.serhiiromanchuk.echojournal.domain.repository.TopicRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import com.serhiiromanchuk.echojournal.presentation.core.utils.MoodUiModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodType
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.BottomSheetClosed
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.BottomSheetOpened
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.CreateTopicClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.DescriptionValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.MoodSelected
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.PauseClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.PlayClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.ResumeClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.SheetConfirmedClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TagClearClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TitleValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TopicClicked
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.EntryUiEvent.TopicValueChanged
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntrySheetState
import com.serhiiromanchuk.echojournal.presentation.screens.entry.handling.state.EntryUiState
import com.serhiiromanchuk.echojournal.utils.Constants
import com.serhiiromanchuk.echojournal.utils.InstantFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.io.File

typealias EntryBaseViewModel = BaseViewModel<EntryUiState, EntryUiEvent, EntryActionEvent>

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = EntryViewModel.EntryViewModelFactory::class)
class EntryViewModel @AssistedInject constructor(
    @Assisted("audioFilePath") val audioFilePath: String,
    @Assisted("amplitudeLogFilePath") val amplitudeLogFilePath: String,
    @Assisted val entryId: Long,
    private val entryRepository: EntryRepository,
    private val topicRepository: TopicRepository,
    private val audioPlayer: AudioPlayer,
    settingsRepository: SettingsRepository
) : EntryBaseViewModel() {
    override val initialState: EntryUiState
        get() = EntryUiState()

    private val defaultMood = settingsRepository.getMood(Constants.KEY_MOOD_SETTINGS)
    private val defaultTopicsId = settingsRepository.getTopics(Constants.KEY_TOPIC_SETTINGS)

    private val searchQuery = MutableStateFlow("")
    private val searchResults: StateFlow<List<Topic>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                flow {
                    val foundTopics = topicRepository.searchTopics(query)
                    emit(foundTopics)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        audioPlayer.initializeFile(audioFilePath)
        updateState {
            it.copy(
                playerState = currentState.playerState.copy(
                    duration = audioPlayer.getDuration(),
                    amplitudeLogFilePath = amplitudeLogFilePath
                )
            )
        }

        // Set default settings
        launch {
            val defaultTopics = topicRepository.getTopicsByIdList(defaultTopicsId)
            updateState {
                it.copy(
                    entrySheetState = currentState.entrySheetState.copy(activeMood = defaultMood.toMoodUiModel()),
                    currentTopics = defaultTopics,
                )
            }
        }

        // Subscribe to topic search results
        launch {
            searchResults.collect {
                updateState { it.copy(foundTopics = searchResults.value) }
            }
        }

        // Set a listener to handle actions when audio playback completes.
        audioPlayer.setOnCompletionListener {
            updatePlayerStateAction(PlayerState.Action.Initializing)
            audioPlayer.stop()
        }

        // Subscribe to the current position of the entry
        launch {
            audioPlayer.currentPositionFlow.collect { positionMillis ->
                val currentPositionText =
                    InstantFormatter.formatMillisToTime(positionMillis.toLong())
                updateState {
                    it.copy(
                        playerState = currentState.playerState.copy(
                            currentPosition = positionMillis,
                            currentPositionText = currentPositionText
                        )
                    )
                }
            }
        }
    }

    override fun onEvent(event: EntryUiEvent) {
        when (event) {
            BottomSheetClosed -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState))
            }
            is BottomSheetOpened -> updateState {
                it.copy(entrySheetState = toggleSheetState(currentState.entrySheetState, event.mood))
            }
            is SheetConfirmedClicked -> setCurrentMood(event.mood)

            is MoodSelected -> updateActiveMood(event.mood)
            is TitleValueChanged -> updateState { it.copy(titleValue = event.value) }
            is DescriptionValueChanged -> updateState { it.copy(descriptionValue = event.value) }

            is TopicValueChanged -> updateTopic(event.value)
            is TagClearClicked -> updateState {
                it.copy(currentTopics = currentState.currentTopics - event.topic)
            }
            is TopicClicked -> updateCurrentTopics(event.topic)
            CreateTopicClicked -> addNewTopic()

            PlayClicked -> playAudio()
            PauseClicked -> pauseAudio()
            ResumeClicked -> resumeAudio()

            is EntryUiEvent.SaveButtonClicked -> saveEntry(event.outputDir)
        }
    }

    private fun saveEntry(outputDir: File) {
        val newAudioFilePath = renameFile(outputDir, audioFilePath, "audio")
        val newAmplitudeLogFilePath = renameFile(outputDir, amplitudeLogFilePath, "amplitude")
        val topics = currentState.currentTopics.map { it.name }

        val newEntry = Entry(
            title = currentState.titleValue,
            moodType = currentState.currentMood.toMoodType(),
            audioFilePath = newAudioFilePath,
            audioDuration = currentState.playerState.duration,
            amplitudeLogFilePath = newAmplitudeLogFilePath,
            description = currentState.descriptionValue,
            topics = topics
        )

        launch {
            entryRepository.upsertEntry(newEntry)
            sendActionEvent(EntryActionEvent.NavigateBack)
        }
    }

    private fun playAudio() {
        updatePlayerStateAction(PlayerState.Action.Playing)
        audioPlayer.play()
    }

    private fun pauseAudio() {
        updatePlayerStateAction(PlayerState.Action.Paused)
        audioPlayer.pause()
    }

    private fun resumeAudio() {
        updatePlayerStateAction(PlayerState.Action.Resumed)
        audioPlayer.resume()
    }

    private fun addNewTopic() {
        val newTopic = Topic(name = currentState.topicValue)
        updateCurrentTopics(newTopic)
        launch {
            topicRepository.insertTopic(newTopic)
        }
    }

    private fun updateCurrentTopics(newTopic: Topic) {
        updateState {
            it.copy(
                currentTopics = currentState.currentTopics + newTopic,
                topicValue = ""
            )
        }
    }


    private fun setCurrentMood(mood: MoodUiModel) {
        updateState {
            it.copy(
                currentMood = mood,
                entrySheetState = toggleSheetState(currentState.entrySheetState)
            )
        }
    }

    private fun updateActiveMood(mood: MoodUiModel) {
        updateState {
            it.copy(
                entrySheetState = currentState.entrySheetState.copy(activeMood = mood)
            )
        }
    }

    private fun updateTopic(topic: String) {
        updateState { it.copy(topicValue = topic) }
        searchQuery.value = topic
    }

    private fun toggleSheetState(
        state: EntrySheetState,
        activeMood: MoodUiModel = MoodUiModel.Undefined
    ): EntrySheetState {
        return state.copy(
            isOpen = !state.isOpen,
            activeMood = activeMood
        )
    }

    private fun updatePlayerStateAction(action: PlayerState.Action) {
        val updatedPlayerState = currentState.playerState.copy(action = action)
        updateState { it.copy(playerState = updatedPlayerState) }
    }

    private fun renameFile(outputDir: File, filePath: String, newValue: String): String {
        val file = File(filePath)
        val newFileName = file.name.replace("temp", newValue)
        val newFile = File(outputDir, newFileName)
        val isRenamed = file.renameTo(newFile)

        return if (isRenamed) newFile.absolutePath else throw IllegalStateException("Failed to rename ${file.name}.")
    }

    @AssistedFactory
    interface EntryViewModelFactory {
        fun create(
            @Assisted("audioFilePath") entryFilePath: String,
            @Assisted("amplitudeLogFilePath") amplitudeLogFilePath: String,
            entryId: Long
        ): EntryViewModel
    }
}