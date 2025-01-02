package com.serhiiromanchuk.echojournal.presentation.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.core.components.EchoFAB
import com.serhiiromanchuk.echojournal.presentation.core.components.EchoTopBar
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EmptyScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState

@Composable
fun HomeScreenRoot(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = hiltViewModel()

    BaseContentLayout(
        modifier = modifier,
        viewModel = viewModel,
        topBar = {
            EchoTopBar(
                title = stringResource(R.string.your_echojournal),
                onSettingsClick = {}
            )
        },
        floatingActionButton = {
            EchoFAB(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { uiState ->
        HomeScreen(
            uiState = uiState,
            onEvent = {}
        )
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    if (uiState.echos.isEmpty()) {
        EmptyScreen(modifier = Modifier.padding(16.dp))
    }
}