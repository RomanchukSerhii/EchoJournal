package com.serhiiromanchuk.echojournal.presentation

import android.content.Context
import android.os.Bundle
import android.os.Environment
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            deleteTempFiles(this@MainActivity)
        }

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

    private fun deleteTempFiles(context: Context) {
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        if (outputDir != null && outputDir.exists()) {
            val temFiles = outputDir.listFiles { file ->
                file.name.startsWith("temp")
            }
            temFiles?.forEach { file ->
                file.delete()
            }
        } else throw IllegalStateException("Music directory does not exist or is not accessible.")
    }
}