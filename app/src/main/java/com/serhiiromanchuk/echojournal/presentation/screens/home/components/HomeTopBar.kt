package com.serhiiromanchuk.echojournal.presentation.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R

@Composable
fun HomeTopBar(
    title: String,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Title
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        // Settings icon
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { onSettingsClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = stringResource(R.string.settings_button),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}