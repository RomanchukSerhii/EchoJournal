package com.serhiiromanchuk.echojournal.presentation.screens.home

import android.net.Uri
import com.serhiiromanchuk.echojournal.domain.audio.AudioRecorder
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.domain.repository.EntryRepository
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodType
import com.serhiiromanchuk.echojournal.presentation.core.utils.toMoodUiModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent.*
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.FilterState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeSheetState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState.EntryHolderState
import com.serhiiromanchuk.echojournal.utils.StopWatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.Instant
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

    private val moodFiltersChecked = MutableStateFlow(listOf<FilterState.FilterItem>())
    private val topicFiltersChecked = MutableStateFlow(listOf<FilterState.FilterItem>())

    init {
        launch {
            entryRepository.getEntries().collect { entries ->
                val topics = mutableListOf<String>()

                val sortedEntries = entries
                    .groupBy { entry ->
                        entry.topics.forEach { topic ->
                            if (!topics.contains(topic)) topics.add(topic)
                        }

                        val localDate =
                            entry.creationTimestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                        localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                    }
                    .mapValues { (_, entryList) ->
                        entryList.map { EntryHolderState(it) }
                    }

                val updatedTopicFilterItems = addNewTopicFilterItems(topics)
                updateState {
                    it.copy(
                        entries = sortedEntries,
                        filterState = currentState.filterState.copy(topicFilterItems = updatedTopicFilterItems)
                    )
                }
            }
        }

        launch {
            combine(
                moodFiltersChecked,
                topicFiltersChecked
            ) { moodFiltersChecked, topicFiltersChecked ->
                val moodFilters = moodFiltersChecked.map { it.title.toMoodUiModel().toMoodType() }
                val topicFilters = topicFiltersChecked.map { it.title }

                if (moodFiltersChecked.isEmpty() && topicFiltersChecked.isEmpty()) {
                    updateState { it.copy(isFilterActive = false) }
                } else {
                    val filteredEntries =
                        filterEntries(currentState.entries, moodFilters, topicFilters)
                    updateState {
                        it.copy(
                            filteredEntries = filteredEntries,
                            isFilterActive = true
                        )
                    }
                }
            }
        }
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
        }
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
        val updatedMoodItems = currentState.filterState.moodFilterItems.map {
            if (it.isChecked) it.copy(isChecked = false) else it
        }

        updateMoodFilterItems(updatedMoodItems, false)
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
        val updatedTopicItems = currentState.filterState.topicFilterItems.map {
            if (it.isChecked) it.copy(isChecked = false) else it
        }

        updateTopicFilterItems(updatedTopicItems, false)
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
            sendActionEvent(
                HomeActionEvent.NavigateToEntryScreen(
                    Uri.encode(audioFilePath), Uri.encode(amplitudeLogFilePath)
                )
            )
        }
    }

    private fun toggleSheetState() {
        val updatedSheetState =
            currentState.homeSheetState.copy(
                isVisible = !currentState.homeSheetState.isVisible,
                isRecording = true
            )

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

    private fun filterEntries(
        entries: Map<Instant, List<EntryHolderState>>,
        moodFilters: List<MoodType>,
        topicFilters: List<String>
    ): Map<Instant, List<EntryHolderState>> {
        return entries.mapValues { (_, entryList) ->
            entryList.filter { entryHolderState ->
                val entry = entryHolderState.entry
                entry.moodType in moodFilters && entry.topics.any { it in topicFilters }
            }
        }.filterValues { it.isNotEmpty() }
    }
}