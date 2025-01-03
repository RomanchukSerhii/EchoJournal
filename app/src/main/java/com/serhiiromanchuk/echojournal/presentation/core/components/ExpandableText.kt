package com.serhiiromanchuk.echojournal.presentation.core.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.serhiiromanchuk.echojournal.utils.Constants.DEFAULT_MINIMUM_TEXT_LINE

/**

 * @param text The text to be displayed.
 * @param expandText The text to display for the expand text button.
 * @param collapseText The text to display for the collapse text button.
 * @param style The TextStyle to apply to the text.
 * @param clickableTextStyle The SpanStyle for the expand and collapse text button.
 * @param collapsedMaxLine The maximum number of lines to display when collapsed.
 * @param fontSize The font size of the text.
 * @param textAlign The alignment of the text.
 * @param fontStyle The FontStyle to apply to the text.
 * @param modifier Modifier for the Box containing the text.

 */
@Composable
fun ExpandableText(
    text: String,
    expandText: String = " Show More",
    collapseText: String = " Show Less",
    style: TextStyle = LocalTextStyle.current,
    clickableTextStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    fontStyle: FontStyle? = null,
    modifier: Modifier = Modifier,
) {
    // State variables to track the expanded state, clickable state, and last character index.
    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    val annotatedText = buildAnnotatedString {
        if (isClickable) {
            if (isExpanded) {
                // Display the full text and "Show Less" button when expanded.
                append(text)
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show Less",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = clickableTextStyle) { append(collapseText) }
                }
            } else {
                // Display truncated text and "Show More" button when collapsed.
                val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(expandText.length)
                    .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                append(adjustText)
                append("...")
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show More",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = clickableTextStyle) { append(expandText) }
                }
            }
        } else {
            // Display the full text when not clickable.
            append(text)
        }
    }

    Text(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        text = annotatedText,
        // Set max lines based on the expanded state.
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
        fontStyle = fontStyle,
        // Callback to determine visual overflow and enable click ability.
        onTextLayout = { textLayoutResult ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                isClickable = true
                lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
            }
        },
        style = style,
        textAlign = textAlign,
        fontSize = fontSize
    )
}