package com.serhiiromanchuk.echojournal.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object Home : Screen(ROUTE_HOME)

    data object Settings : Screen(ROUTE_SETTINGS)

    data object Entry : Screen(ROUTE_ENTRY) {
        const val AUDIO_FILE_PATH = "audio_file_path"
        const val AMPLITUDE_LOG_FILE_PATH = "amplitude_log_file_path"
        const val ID = "id"

        val routeWithArgs = "$route/{$AUDIO_FILE_PATH}/{$AMPLITUDE_LOG_FILE_PATH}/{$ID}"

        val arguments = listOf(
            navArgument(AUDIO_FILE_PATH) { type = NavType.StringType },
            navArgument(ID) { type = NavType.LongType }
        )
    }

    companion object {
        private const val ROUTE_HOME = "home_screen"
        private const val ROUTE_SETTINGS = "settings_screen"
        private const val ROUTE_ENTRY = "entry_screen"
    }
}