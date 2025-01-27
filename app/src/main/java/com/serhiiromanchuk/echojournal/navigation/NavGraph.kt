package com.serhiiromanchuk.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

@Composable
fun RootAppNavigation(
    navigationState: NavigationState,
    isDataLoaded: () -> Unit,
    isLaunchedFromWidget: Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navigationState.navHostController,
        startDestination = Screen.Home.route
    ) {
        homeRoute(
            navigationState = navigationState,
            isDataLoaded = isDataLoaded,
            isLaunchedFromWidget = isLaunchedFromWidget,
            modifier = modifier
        )
        entryRoute(
            modifier = modifier,
            navigationState = navigationState
        )
        settingsRoute(
            modifier = modifier,
            navigationState = navigationState
        )
    }
}