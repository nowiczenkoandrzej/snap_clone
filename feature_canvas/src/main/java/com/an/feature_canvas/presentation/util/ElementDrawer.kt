package com.an.feature_canvas.presentation.util

import android.content.Context
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import com.an.core_editor.presentation.UiElement
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.UiTextModel
import kotlin.math.abs
import kotlin.math.roundToInt

fun DrawScope.elementDrawer(
    textMeasurer: TextMeasurer,
    elements: List<UiElement>,
    selectedElementIndex: Int?,
    showElementDetails: Boolean
) {
    clipRect {
        elements.forEachIndexed { index, element ->
            val rotationAngle = if(abs(element.rotationAngle % 90) < 3 || element.rotationAngle % 90 > 87) {
                val rotations = (element.rotationAngle / 90).roundToInt()
                (rotations * 90).toFloat()
            } else element.rotationAngle

            withTransform({
                rotate(
                    degrees = rotationAngle,
                    pivot = element.center()
                )
                scale(
                    scale = element.scale,
                    pivot = element.center()
                )
            }) {
                when(element) {
                    is UiImageModel -> {
                        if(element.bitmap == null) return@withTransform
                        drawImage(
                            image = element.bitmap!!.asImageBitmap(),
                            topLeft = element.position,
                            alpha = element.alpha
                        )
                    }
                    is UiTextModel -> {
                        val annotatedText = AnnotatedString(
                            text = element.text,
                            spanStyle = SpanStyle(
                                color = element.fontColor,
                                fontSize = element.fontSize.toSp(),
                                fontFamily = element.fontItem.fontFamily
                            )
                        )

                        val layoutResult = textMeasurer.measure(
                            text = annotatedText,
                        )
                        drawIntoCanvas { canvas ->
                            canvas.save()
                            canvas.translate(element.position.x, element.position.y)
                            layoutResult.multiParagraph.paint(canvas)
                            canvas.restore()
                        }
                    }
                }
                if(index == selectedElementIndex && showElementDetails) {
                    val text = "angle: ${rotationAngle.toInt()}°, scale: ${String.format("%.2f", element.scale)}"

                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 32f
                        isAntiAlias = true
                        textAlign = android.graphics.Paint.Align.LEFT
                    }

                    val bounds = android.graphics.Rect()
                    paint.getTextBounds(text, 0, text.length, bounds)
                    val textWidth = bounds.width().toFloat()
                    val textHeight = bounds.height().toFloat()

                    val x = 32f
                    val y = 32f

                    drawContext.canvas.nativeCanvas.drawRect(
                        x - textWidth,
                        y - textHeight,
                        x + textWidth,
                        y + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.LTGRAY //
                        }
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        text,
                        x,
                        y,
                        paint
                    )

                }
            }
        }
    }
}