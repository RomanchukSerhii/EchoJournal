package com.serhiiromanchuk.echojournal.presentation.screens.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.navigation.NavigationState
import com.serhiiromanchuk.echojournal.navigation.Screen
import com.serhiiromanchuk.echojournal.presentation.core.base.BaseContentLayout
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EchoFilter
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.EmptyHomeScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeFAB
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.HomeTopBar
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.JournalEntries
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.PermissionDialog
import com.serhiiromanchuk.echojournal.presentation.screens.home.components.RecordingBottomSheet
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.HomeUiEvent
import com.serhiiromanchuk.echojournal.presentation.screens.home.handling.state.HomeUiState

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {
    val viewModel: HomeViewModel = hiltViewModel()

    BaseContentLayout(
        modifier = modifier,
        viewModel = viewModel,
        topBar = {
            HomeTopBar(
                title = stringResource(R.string.your_echojournal),
                onSettingsClick = {
                    navigationState.navigateTo(Screen.Settings.route)
                }
            )
        },
        floatingActionButton = {
            val context = LocalContext.current
            val activity = context as Activity

            var isPermissionDialogOpened by remember { mutableStateOf(false) }

            val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    isPermissionDialogOpened = !isGranted
                    if (isGranted) {
                        viewModel.onEvent(HomeUiEvent.BottomSheetToggled)
                    }
                }
            )

            HomeFAB(
                onClick = {
                    recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (isPermissionDialogOpened) {
                PermissionDialog(
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                    ),
                    onDismiss = { isPermissionDialogOpened = false },
                    onOkClick = {
                        isPermissionDialogOpened = false
                        recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    },
                    onGoToAppSettingsClick = {
                        isPermissionDialogOpened = false
                        activity.openAppSettings()
                    }
                )
            }
        }
    ) { uiState ->
        if (uiState.entries.isEmpty()) {
            EmptyHomeScreen(modifier = Modifier.padding(16.dp))
        } else {
            HomeScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
            RecordingBottomSheet(
                homeSheetState = uiState.homeSheetState,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column {
        EchoFilter(
            filterState = uiState.filterState,
            onEvent = onEvent
        )
        JournalEntries(
            entryNotes = uiState.entries,
            onEvent = onEvent
        )
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}