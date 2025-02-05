package com.serhiiromanchuk.echojournal.presentation.screens.home

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.serhiiromanchuk.echojournal.domain.audio.AudioPlayer
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.state.PlayerState
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodType
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.ActionButtonStartRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.ActionButtonStopRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.EntryPauseClick
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.EntryPlayClick
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.EntryResumeClick
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.MoodFilterItemClicked
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.MoodsFilterClearClicked
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.MoodsFilterToggled
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PauseRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.PermissionDialogOpened
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.ResumeRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StartRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.StopRecording
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.TopicFilterItemClicked
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.TopicsFilterClearClicked
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.TopicsFilterToggled
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiState.EntryHolderState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiState.FilterState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiState.HomeSheetState
import com.serhiiromanchuk.echojournal.utils.InstantFormatter
import com.serhiiromanchuk.echojournal.utils.StopWatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

private typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
) : HomeBaseViewModel() {
    override val initialState: HomeUiState
        get() = HomeUiState()

    private val stopWatch = StopWatch()
    private var stopWatchJob: Job? = null

    private val moodFiltersChecked = MutableStateFlow<List<FilterState.FilterItem>>(emptyList())
    private val topicFiltersChecked = MutableStateFlow<List<FilterState.FilterItem>>(emptyList())
    private val filteredEntries = MutableStateFlow<Map<Instant, List<EntryHolderState>>?>(emptyMap())
    private var fetchedEntries: Map<Instant, List<EntryHolderState>> = emptyMap()

    private var playingEntryId = MutableStateFlow<Long?>(null)

    // FEEDBACK: Opt to use .onStart{} on screen state to trigger setup logic
    // Makes VM easier to test
    init {
        observeEntries()
        observeFilters()
        setupAudioPlayerListeners()
        observeAudioPlayerCurrentPosition()
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            MoodsFilterToggled -> toggleMoodFilter()
            is MoodFilterItemClicked -> toggleMoodItemCheckedState(event.title)
            MoodsFilterClearClicked -> clearMoodFilter()

            TopicsFilterToggled -> toggleTopicFilter()
            is TopicFilterItemClicked -> toggleTopicItemCheckedState(event.title)
            TopicsFilterClearClicked -> clearTopicFilter()

            is PermissionDialogOpened -> updateState { it.copy(isPermissionDialogOpen = event.isOpen) }

            StartRecording -> {
                toggleSheetState()
                startRecording()
            }
            PauseRecording -> pauseRecording()
            ResumeRecording -> resumeRecording()
            is StopRecording -> {
                toggleSheetState()
                stopRecording(event.saveFile)
            }

            ActionButtonStartRecording -> startRecording()
            is ActionButtonStopRecording -> stopRecording(event.saveFile)

            is EntryPlayClick -> playEntry(event.entryId)
            is EntryPauseClick -> pauseEntry(event.entryId)
            is EntryResumeClick -> resumeEntry(event.entryId)
        }
    }

    private fun observeEntries() {
        var isFirstLoad = true

        entryRepository.getEntries()
            .combine(filteredEntries) { dataEntries, currentFilteredEntries ->
                val topics = mutableSetOf<String>()

                val sortedEntries = if (currentFilteredEntries != null && currentFilteredEntries.isEmpty()) {
                    fetchedEntries = groupEntriesByDate(dataEntries, topics)
                    fetchedEntries
                } else currentFilteredEntries ?: emptyMap()

                val updatedTopicFilterItems = addNewTopicFilterItems(topics.toList())
                updateState {
                    it.copy(
                        entries = sortedEntries,
                        filterState = currentState.filterState.copy(topicFilterItems = updatedTopicFilterItems)
                    )
                }

                if (isFirstLoad) {
                    sendActionEvent(HomeActionEvent.DataLoaded)
                    isFirstLoad = false
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFilters() {
        combine(moodFiltersChecked, topicFiltersChecked) { moodFilters, topicFilters ->
            val moodTypes = moodFilters.map { it.title.toMoodUiModel().toMoodType() }
            val topicTitles = topicFilters.map { it.title }
            val isFilterActive = moodFilters.isNotEmpty() || topicFilters.isNotEmpty()

            filteredEntries.value = if (isFilterActive) {
                getFilteredEntries(fetchedEntries, moodTypes, topicTitles)
            } else emptyMap()

            updateState { it.copy(isFilterActive = isFilterActive) }
        }.launchIn(viewModelScope)
    }

    private fun setupAudioPlayerListeners() {
        // Set a listener to handle actions when audio playback completes.
        audioPlayer.setOnCompletionListener {
            playingEntryId.value?.let { entryId ->
                updatePlayerStateAction(entryId, PlayerState.Action.Initializing)
                audioPlayer.stop()
            }
        }
    }

    private fun observeAudioPlayerCurrentPosition() {
        // Subscribe to the current position of the entry
        launch {
            audioPlayer.currentPositionFlow.collect { positionMillis ->
                val currentPositionText =
                    InstantFormatter.formatMillisToTime(positionMillis.toLong())
                playingEntryId.value?.let { entryId ->
                    updatePlayerStateCurrentPosition(
                        entryId = entryId,
                        currentPosition = positionMillis,
                        currentPositionText = currentPositionText
                    )
                }

            }
        }
    }

    private fun groupEntriesByDate(
        entries: List<Entry>,
        topics: MutableSet<String>
    ): Map<Instant, List<EntryHolderState>> {
        return entries.groupBy { entry ->
            entry.topics.forEach { topic ->
                if (!topics.contains(topic)) topics.add(topic)
            }
            entry.creationTimestamp
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
        }.mapValues { (_, entryList) ->
            entryList.map { EntryHolderState(it) }
        }
    }

    private fun toggleMoodFilter() {
        val updatedFilterState = currentState.filterState.copy(
            isMoodsOpen = !currentState.filterState.isMoodsOpen,
            isTopicsOpen = false
        )
        updateState { it.copy(filterState = updatedFilterState) }
    }

    private fun toggleMoodItemCheckedState(title: String) {
        val updatedMoodItems = currentState.filterState.moodFilterItems.map {
            if (it.title == title) it.copy(isChecked = !it.isChecked) else it
        }

        moodFiltersChecked.value = updatedMoodItems.filter { it.isChecked }
        updateMoodFilterItems(updatedMoodItems)
    }

    private fun clearMoodFilter() {
        moodFiltersChecked.value = emptyList()
        val updatedMoodItems = currentState.filterState.moodFilterItems.map {
            if (it.isChecked) it.copy(isChecked = false) else it
        }

        updateMoodFilterItems(updatedMoodItems, false)
    }

    private fun toggleTopicFilter() {
        val updatedFilterState = currentState.filterState.copy(
            isTopicsOpen = !currentState.filterState.isTopicsOpen,
            isMoodsOpen = false
        )
        updateState { it.copy(filterState = updatedFilterState) }
    }

    private fun toggleTopicItemCheckedState(title: String) {
        val updatedTopicItems = currentState.filterState.topicFilterItems.map {
            if (it.title == title) it.copy(isChecked = !it.isChecked) else it
        }

        topicFiltersChecked.value = updatedTopicItems.filter { it.isChecked }
        updateTopicFilterItems(updatedTopicItems)
    }

    private fun clearTopicFilter() {
        topicFiltersChecked.value = emptyList()
        val updatedTopicItems = currentState.filterState.topicFilterItems.map {
            if (it.isChecked) it.copy(isChecked = false) else it
        }

        updateTopicFilterItems(updatedTopicItems, false)
    }

    private fun toggleSheetState() {
        val updatedSheetState =
            currentState.homeSheetState.copy(
                isVisible = !currentState.homeSheetState.isVisible,
                isRecording = true
            )

        updateHomeSheetState(updatedSheetState)
    }

    private fun startRecording() {
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
        if (saveFile) {
            val amplitudeLogFilePath = audioRecorder.getAmplitudeLogFilePath()
            stopEntriesPlaying()
            sendActionEvent(
                HomeActionEvent.NavigateToEntryScreen(
                    Uri.encode(audioFilePath), Uri.encode(amplitudeLogFilePath)
                )
            )
        }
    }

    private fun playEntry(entryId: Long) {
        if (audioPlayer.isPlaying()) {
            stopEntriesPlaying()
            audioPlayer.stop()
        }
        playingEntryId.value = entryId
        updatePlayerStateAction(entryId, PlayerState.Action.Playing)

        val audioFilePath = getCurrentEntryHolderState(entryId).entry.audioFilePath
        audioPlayer.initializeFile(audioFilePath)
        audioPlayer.play()
    }

    private fun pauseEntry(entryId: Long) {
        updatePlayerStateAction(entryId, PlayerState.Action.Paused)
        audioPlayer.pause()
    }

    private fun resumeEntry(entryId: Long) {
        updatePlayerStateAction(entryId, PlayerState.Action.Resumed)
        audioPlayer.resume()
    }

    private fun stopEntriesPlaying() {
        val updatedEntries = currentState.entries.mapValues { (_, entryList) ->
            entryList.map { entryHolderState ->
                if (entryHolderState.playerState.action == PlayerState.Action.Playing
                    || entryHolderState.playerState.action == PlayerState.Action.Paused
                ) {
                    val updatedPlayerState =
                        entryHolderState.playerState.copy(
                            action = PlayerState.Action.Initializing,
                            currentPosition = 0,
                            currentPositionText = "00:00"
                        )
                    entryHolderState.copy(playerState = updatedPlayerState)
                } else entryHolderState
            }
        }
        updateState { it.copy(entries = updatedEntries) }
    }

    private fun updatePlayerStateCurrentPosition(
        entryId: Long,
        currentPosition: Int,
        currentPositionText: String
    ) {
        val entryHolderState = getCurrentEntryHolderState(entryId)
        val updatedPlayerState = entryHolderState.playerState.copy(
            currentPosition = currentPosition,
            currentPositionText = currentPositionText
        )
        updatePlayerState(entryId, updatedPlayerState)
    }

    private fun updatePlayerStateAction(entryId: Long, action: PlayerState.Action) {
        val entryHolderState = getCurrentEntryHolderState(entryId)
        val updatedPlayerState = entryHolderState.playerState.copy(action = action)
        updatePlayerState(entryId, updatedPlayerState)
    }

    private fun updatePlayerState(entryId: Long, newPlayerState: PlayerState) {
        val updatedEntries = currentState.entries.mapValues { (_, entryList) ->
            entryList.map { entryHolderState ->
                if (entryHolderState.entry.id == entryId) {
                    entryHolderState.copy(playerState = newPlayerState)
                } else entryHolderState
            }
        }
        updateState { it.copy(entries = updatedEntries) }
    }

    private fun addNewTopicFilterItems(topics: List<String>): List<FilterState.FilterItem> {
        val currentTopics = currentState.filterState.topicFilterItems.map { it.title }
        val newTopicItems = currentState.filterState.topicFilterItems.toMutableList()
        topics.forEach { topic ->
            if (!currentTopics.contains(topic)) {
                newTopicItems.add(FilterState.FilterItem(topic))
            }
        }
        return newTopicItems
    }

    private fun updateTopicFilterItems(
        updatedItems: List<FilterState.FilterItem>,
        isOpen: Boolean = true
    ) {
        updateState {
            it.copy(
                filterState = currentState.filterState.copy(
                    topicFilterItems = updatedItems,
                    isTopicsOpen = isOpen
                )
            )
        }
    }

    private fun updateMoodFilterItems(
        updatedItems: List<FilterState.FilterItem>,
        isOpen: Boolean = true
    ) {
        updateState {
            it.copy(
                filterState = currentState.filterState.copy(
                    moodFilterItems = updatedItems,
                    isMoodsOpen = isOpen
                )
            )
        }
    }

    private fun getCurrentEntryHolderState(entryId: Long): EntryHolderState {
        return currentState.entries.values
            .flatten()
            .find { it.entry.id == entryId }
            ?: throw IllegalArgumentException("Audio file path not found for entry ID: $entryId")
    }

    private fun toggleRecordingState() {
        val updatedSheetState =
            currentState.homeSheetState.copy(isRecording = !currentState.homeSheetState.isRecording)
        updateHomeSheetState(updatedSheetState)
    }

    private fun updateHomeSheetState(updatedSheetState: HomeSheetState) {
        updateState { it.copy(homeSheetState = updatedSheetState) }
    }

    private fun getFilteredEntries(
        entries: Map<Instant, List<EntryHolderState>>,
        moodFilters: List<MoodType>,
        topicFilters: List<String>
    ): Map<Instant, List<EntryHolderState>>? {
        return entries.mapValues { (_, entryList) ->
            entryList.filter { entryHolderState ->
                val entry = entryHolderState.entry
                entry.moodType in moodFilters || entry.topics.any { it in topicFilters }
            }
        }.filterValues { it.isNotEmpty() }.ifEmpty { null }
    }
}