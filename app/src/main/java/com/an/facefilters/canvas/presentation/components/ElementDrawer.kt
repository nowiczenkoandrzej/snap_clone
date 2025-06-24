package com.an.facefilters.canvas.presentation.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.domain.model.Sticker
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.presentation.util.drawPencil
import com.an.facefilters.canvas.presentation.util.loadPngAssetAsImageBitmap

fun ElementDrawer(
    drawScope: DrawScope,
    textMeasurer: TextMeasurer,
    elements: List<Element>,
    paths: List<PathData>,
    currentPath: PathData? = null,
    context: Context
) {


    drawScope.clipRect {
        elements.forEachIndexed { index, element ->

            withTransform({
                rotate(
                    degrees = element.rotationAngle,
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
                        val layoutResult = textMeasurer.measure(
                            text = AnnotatedString(element.text),
                            style = element.textStyle
                        )

                        drawIntoCanvas { canvas ->
                            canvas.save()
                            canvas.translate(element.p1.x, element.p1.y)
                            layoutResult.multiParagraph.paint(canvas)
                            canvas.restore()
                        }
                    }
                    is Sticker -> {
                        drawImage(
                            image = loadPngAssetAsImageBitmap(context = context, fileName = element.pngAsset).asImageBitmap(),
                            topLeft = element.p1,
                            alpha = element.alpha
                        )
                    }
                }
            }
        }

        paths.forEach { path ->
            drawPencil(
                path = path.path,
                color = path.color,
                thickness = path.thickness
            )
        }
        currentPath?.let { path ->
            drawPencil(
                path = path.path,
                color = path.color,
                thickness = path.thickness
            )
        }
    }

}