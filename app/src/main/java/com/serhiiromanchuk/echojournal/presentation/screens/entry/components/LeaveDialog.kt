package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LeaveDialog(
    headline: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    confirmButtonName: String = "",
    cancelButtonName: String = ""
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                DialogHeader(
                    headline = headline,
                    supportingText = supportingText
                )

                Spacer(modifier = Modifier.height(16.dp))

                DialogButtons(
                    cancelButtonName = cancelButtonName,
                    confirmButtonName = confirmButtonName,
                    onCancelClicked = onDismissRequest,
                    onConfirmClicked = onConfirm
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(
    modifier: Modifier = Modifier,
    headline: String,
    supportingText: String? = null
) {
    Column(
        modifier = modifier
    ) {
        // Headline
        Text(
            text = headline,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (supportingText != null) {
            Spacer(modifier = Modifier.height(16.dp))

            // SupportingText
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
private fun DialogButtons(
    modifier: Modifier = Modifier,
    cancelButtonName: String,
    confirmButtonName: String,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Right
    ) {
        // Cancel text button
        TextButton(
            onClick = onCancelClicked
        ) {
            Text(
                text = cancelButtonName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Confirm text button
        TextButton(
            onClick = onConfirmClicked
        ) {
            Text(
                text = confirmButtonName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}