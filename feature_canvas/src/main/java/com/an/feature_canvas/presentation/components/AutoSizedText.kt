package com.an.feature_canvas.presentation.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoSizeText(
    text: String,
    color: Color,
    fontFamily: FontFamily = FontFamily.Default,
    maxFontSize: TextUnit = 40.sp,
    minFontSize: TextUnit = 10.sp,
    maxLines: Int = 3,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    var fontSize by remember { mutableStateOf(maxFontSize) }

    BoxWithConstraints(modifier = modifier) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val maxHeightPx = constraints.maxHeight.toFloat()

        LaunchedEffect(text, maxWidthPx, maxHeightPx) {
            var currentSize = maxFontSize
            while (currentSize > minFontSize) {
                val textLayout = textMeasurer.measure(
                    text = AnnotatedString(text),
                    style = TextStyle(
                        fontSize = currentSize,
                        fontFamily = fontFamily
                    ),
                    constraints = Constraints(
                        maxWidth = maxWidthPx.toInt()
                    )
                )

                val lineCount = textLayout.lineCount
                val textHeight = textLayout.size.height

                if (lineCount <= maxLines && textHeight <= maxHeightPx) {
                    break
                }

                currentSize *= 0.9f
            }
            fontSize = currentSize
        }

        Text(
            text = text,
            fontSize = fontSize,
            color = color,
            fontFamily = fontFamily,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}