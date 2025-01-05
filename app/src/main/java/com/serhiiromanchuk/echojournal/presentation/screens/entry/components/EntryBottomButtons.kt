package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serhiiromanchuk.echojournal.R
import com.serhiiromanchuk.echojournal.presentation.core.components.PrimaryButton
import com.serhiiromanchuk.echojournal.presentation.core.components.SecondaryButton

@Composable
fun EntryBottomButtons(
    primaryButtonText: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryButtonEnabled: Boolean = true
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SecondaryButton(
            text = stringResource(R.string.cancel),
            onClick = onCancelClick,
            modifier = Modifier.fillMaxHeight()
        )
        PrimaryButton(
            text = primaryButtonText,
            onClick = onConfirmClick,
            modifier = Modifier.weight(1f),
            enabled = primaryButtonEnabled
        )
    }
}