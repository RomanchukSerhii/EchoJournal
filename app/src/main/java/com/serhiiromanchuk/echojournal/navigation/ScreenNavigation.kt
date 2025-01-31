package com.serhiiromanchuk.echojournal.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.serhiiromanchuk.echojournal.presentation.screens.entry.EntryScreenRoot
import com.serhiiromanchuk.echojournal.presentation.screens.home.HomeScreenRoot
import com.serhiiromanchuk.echojournal.presentation.screens.settings.SettingsScreenRoot
import com.serhiiromanchuk.echojournal.utils.Constants

fun NavGraphBuilder.homeRoute(
    navigationState: NavigationState,
    isDataLoaded: () -> Unit,
    isLaunchedFromWidget: Boolean,
    modifier: Modifier = Modifier
) {
    composable(route = Screen.Home.route) {
        HomeScreenRoot(
            navigationState = navigationState,
            isDataLoaded = isDataLoaded,
            isLaunchedFromWidget = isLaunchedFromWidget,
            modifier = modifier
        )
    }
}

fun NavGraphBuilder.entryRoute(
    navigationState: NavigationState,
    modifier: Modifier
) {
    composable(
        route = Screen.Entry.routeWithArgs,
        arguments = Screen.Entry.arguments
    ) { navBackStackEntry ->
        val audioFilePath = navBackStackEntry.arguments?.getString(Screen.Entry.AUDIO_FILE_PATH) ?: ""
        val amplitudeLogFilePath = navBackStackEntry.arguments?.getString(Screen.Entry.AMPLITUDE_LOG_FILE_PATH) ?: ""
        val entryId = navBackStackEntry.arguments?.getLong(Screen.Entry.ID) ?: Constants.UNDEFINED_ENTRY_ID

        EntryScreenRoot(
            modifier = modifier,
            navigationState = navigationState,
            audioFilePath = audioFilePath,
            amplitudeLogFilePath = amplitudeLogFilePath,
            entryId = entryId
        )
    }
}

fun NavGraphBuilder.settingsRoute(
    navigationState: NavigationState,
    modifier: Modifier = Modifier
) {
    composable(route = Screen.Settings.route) {
        SettingsScreenRoot(
            modifier = modifier,
            navigationState = navigationState
        )
    }
}