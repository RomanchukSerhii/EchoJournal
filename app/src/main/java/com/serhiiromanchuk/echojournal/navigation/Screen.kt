package com.serhiiromanchuk.echojournal.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Home : Screen(ROUTE_HOME)
    data object Settings : Screen(ROUTE_SETTINGS)
    data object Entry : Screen(ROUTE_ENTRY) {
        const val FILE_PATH = "file_path"
        const val ID = "id"
        val routeWithArgs = "$route/{$FILE_PATH}/{$ID}"
        val arguments = listOf(
            navArgument(FILE_PATH) { type = NavType.StringType },
            navArgument(ID) { type = NavType.LongType }
        )
    }

    companion object {
        private const val ROUTE_HOME = "home_screen"
        private const val ROUTE_SETTINGS = "settings_screen"
        private const val ROUTE_ENTRY = "entry_screen"
    }
}