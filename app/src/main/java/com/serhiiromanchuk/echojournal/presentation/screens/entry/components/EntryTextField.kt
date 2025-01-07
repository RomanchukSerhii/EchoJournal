@file:OptIn(ExperimentalLayoutApi::class)

package com.serhiiromanchuk.echojournal.presentation.screens.entry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EntryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    hintText: String = "",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    iconSpacing: Dp = Dp.Unspecified
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { isFocused = it.isFocused },
        textStyle = textStyle,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences
        ),
        decorationBox = { innerTextField ->
            Row (
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Leading icon
                leadingIcon?.let {
                    Box(
                        modifier = Modifier
                            .padding(end = iconSpacing)
                    ) {
                        leadingIcon()
                    }
                }

                Box(
                    modifier = Modifier.widthIn(min = 62.dp).align(Alignment.CenterVertically)
                ) {
                    if (value.isEmpty() && !isFocused) {
                        // Hint text
                        Text(
                            text = hintText,
                            style = textStyle.copy(
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                    } else {
                        innerTextField()
                    }
                }
            }
        }
    )
}