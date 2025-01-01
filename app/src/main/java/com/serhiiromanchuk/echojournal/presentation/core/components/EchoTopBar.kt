package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
fun EchoTopBar(
    title: String,
    onSettingsClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        val titleAlignment = if (onBackClick != null) Alignment.Center else Alignment.CenterStart

        // Back icon
        onBackClick?.let {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Text(
            modifier = Modifier.align(titleAlignment),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        // Settings icon
        onSettingsClick?.let {
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
}