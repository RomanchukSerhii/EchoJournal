package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EntryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    hintText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    iconSpacing: Dp = Dp.Unspecified
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Leading icon
                Box(
                    modifier = Modifier
                        .padding(end = iconSpacing)
                ) {
                    leadingIcon()
                }

                // Hint text
                if (value.isEmpty()) {
                    Text(
                        text = hintText,
                        style = textStyle.copy(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                innerTextField()
            }
        }
    )
}