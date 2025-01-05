package com.serhiiromanchuk.echojournal.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.serhiiromanchuk.echojournal.presentation.screens.entry.EntryScreenRoot
import com.serhiiromanchuk.echojournal.presentation.screens.home.HomeScreenRoot
import com.serhiiromanchuk.echojournal.utils.Constants

fun NavGraphBuilder.homeRoute(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {
    composable(route = Screen.Home.route) {
        HomeScreenRoot(
            modifier = modifier,
            navigationState = navigationState
        )
    }
}

fun NavGraphBuilder.entryRoute(
    modifier: Modifier,
    navigationState: NavigationState
) {
    composable(
        route = Screen.Entry.routeWithArgs,
        arguments = Screen.Entry.arguments
    ) { navBackStackEntry ->
        val entryFilePath = navBackStackEntry.arguments?.getString(Screen.Entry.FILE_PATH) ?: ""
        val entryId = navBackStackEntry.arguments?.getLong(Screen.Entry.ID) ?: Constants.UNDEFINED_ENTRY_ID
        EntryScreenRoot(
            modifier = modifier,
            navigationState = navigationState,
            entryFilePath = entryFilePath,
            entryId = entryId
        )
    }
}