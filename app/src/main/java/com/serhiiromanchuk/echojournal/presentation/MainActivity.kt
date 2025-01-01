package com.serhiiromanchuk.echojournal.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.serhiiromanchuk.echojournal.presentation.screens.home.HomeScreenRoot
import com.serhiiromanchuk.echojournal.presentation.theme.EchoJournalTheme
import com.serhiiromanchuk.echojournal.presentation.theme.EchoLightBlue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(EchoLightBlue.toArgb(), EchoLightBlue.toArgb()),
            navigationBarStyle = SystemBarStyle.light(EchoLightBlue.toArgb(), EchoLightBlue.toArgb())
        )

        setContent {
            EchoJournalTheme {
                HomeScreenRoot()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EchoJournalTheme {
        Greeting("Android")
    }
}