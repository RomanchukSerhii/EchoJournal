package com.serhiiromanchuk.echojournal.presentation.screens.home

import com.serhiiromanchuk.echojournal.domain.entity.Entry
import com.serhiiromanchuk.echojournal.domain.entity.MoodType
import com.serhiiromanchuk.echojournal.domain.entity.Topic
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseViewModel
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeActionEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

private typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

@HiltViewModel
class HomeViewModel @Inject constructor() : HomeBaseViewModel() {
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
            is HomeUiEvent.BottomSheetStateChanged -> updateState { it.copy(homeSheetState = event.homeSheetState) }
        }
    }
}