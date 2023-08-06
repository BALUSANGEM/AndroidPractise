package com.nosort.composeone

import android.util.Patterns
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlTextField() {
    var text: String by remember{ mutableStateOf("") }
    TextField(value = text , onValueChange = { newText: String ->
        text = newText
    }, visualTransformation = UrlTransformation(MaterialTheme.colorScheme.secondary))
}

class UrlTransformation(private val color: Color): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            offsetMapping = OffsetMapping.Identity,
            text = buildAnnotatedStringWithUrlHighlighting(text, color),
        )
    }

    private fun buildAnnotatedStringWithUrlHighlighting(
        text: AnnotatedString, color: Color
    ) : AnnotatedString {
        return buildAnnotatedString {
            append(text)
            text.split("\\s+".toRegex()).filter { word ->
                Patterns.WEB_URL.matcher(word).matches()
            }.forEach {
                val startIndex = text.indexOf(it)
                val endIndex = startIndex + it.length
                addStyle(style = SpanStyle(
                    color = color, fontWeight = FontWeight.Bold
                ),startIndex, endIndex)
            }
        }
    }
}