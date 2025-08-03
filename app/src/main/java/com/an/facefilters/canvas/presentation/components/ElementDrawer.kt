package com.an.facefilters.canvas.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.presentation.util.drawPencil
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt


fun ElementDrawer(
    drawScope: DrawScope,
    textMeasurer: TextMeasurer,
    elements: List<Element>,
    context: Context,
    selectedElementIndex: Int?,
    showElementDetails: Boolean
) {



    drawScope.clipRect {
        elements.forEachIndexed { index, element ->
            val rotationAngle = if(abs(element.rotationAngle % 90) < 3 || element.rotationAngle % 90 > 87) {
                val rotations = (element.rotationAngle / 90).roundToInt()
                (rotations * 90).toFloat()
            } else element.rotationAngle


            withTransform({
                rotate(
                    degrees = rotationAngle,
                    pivot = element.pivot()
                )
                scale(
                    scale = element.scale,
                    pivot = element.pivot()
                )
            }) {
                when(element) {
                    is Img -> {
                        drawImage(
                            image = element.bitmap.asImageBitmap(),
                            topLeft = element.p1,
                            alpha = element.alpha
                        )
                    }
                    is TextModel -> {
                        
                        val annotatedText = AnnotatedString(
                            text = element.text,
                            spanStyle = SpanStyle(
                                color = element.textStyle.color,
                                fontSize = element.textStyle.fontSize,
                                fontFamily = element.textStyle.fontFamily
                            )
                        )

                        val layoutResult = textMeasurer.measure(
                            text = annotatedText,
                        )





                        drawIntoCanvas { canvas ->
                            canvas.save()
                            canvas.translate(element.p1.x, element.p1.y)
                            layoutResult.multiParagraph.paint(canvas)
                            canvas.restore()
                        }
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