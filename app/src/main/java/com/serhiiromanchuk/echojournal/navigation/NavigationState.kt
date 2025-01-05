package com.serhiiromanchuk.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.serhiiromanchuk.echojournal.utils.Constants

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route)
    }

    fun popBackStack() = navHostController.popBackStack()

    fun navigateToEntry(filePath: String, entryId: Long = Constants.UNDEFINED_ENTRY_ID) {
        navHostController.navigate("${Screen.Entry.route}/$filePath/$entryId")
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}