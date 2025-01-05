package com.serhiiromanchuk.echojournal.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.serhiiromanchuk.echojournal.navigation.RootAppNavigation
import com.serhiiromanchuk.echojournal.navigation.rememberNavigationState
import com.serhiiromanchuk.echojournal.presentation.theme.EchoJournalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            EchoJournalTheme {
                val navigationState = rememberNavigationState()
                RootAppNavigation(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    navigationState = navigationState
                )
            }
        }
    }
}