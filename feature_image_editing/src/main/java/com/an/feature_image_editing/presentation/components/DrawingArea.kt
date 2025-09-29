package com.an.feature_image_editing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.core_editor.domain.model.PathData
import com.an.feature_image_editing.presentation.util.drawPencil
import kotlin.math.min

@Composable
fun DrawingArea(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    alpha: Float = 1f,
    paths: List<PathData>,
    currentPath: PathData,
    onDrawPath: (Offset, Float) -> Unit,
    onFinishDrawingPath: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckerboardBackground(
            modifier = Modifier
                .fillMaxSize()
        )
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val maxH = constraints.maxHeight.toFloat()
            val maxW = constraints.maxWidth.toFloat()

            val scale = min(
                maxW / bitmap.width.toFloat(),
                maxH / bitmap.height.toFloat()
            )


            var targetHeight = bitmap.height * scale
            var targetWidth = bitmap.width * scale


            val offsetX = (maxW - targetWidth) / 2f
            val offsetY = (maxH - targetHeight) / 2f


            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        adjustViewBounds = true
                        setImageBitmap(bitmap)
                    }
                },
                update = { imageView ->
                    imageView.setImageBitmap(bitmap)
                },
                modifier = Modifier
                    .width(targetWidth.dp)
                    .height(targetHeight.dp)
                    .graphicsLayer(alpha = alpha)


            )
            Canvas(
                modifier = Modifier
                    .width(targetWidth.dp)
                    .height(targetHeight.dp)
                    .graphicsLayer {
                        translationX = offsetX
                        translationY = offsetY
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                val localX = (change.position.x) / scale
                                val localY = (change.position.y) / scale
                                onDrawPath(Offset(localX, localY), scale)
                            },
                            onDragEnd = {
                                onFinishDrawingPath()
                            }
                        )
                    }
            ) {

                clipRect {
                    paths.forEach { path ->
                        drawPencil(
                            path = path.path.map { p ->
                                Offset(p.x * scale, p.y * scale)
                            },
                            color = path.color,
                            thickness = path.thickness * scale
                        )
                    }
                    drawPencil(
                        path = currentPath.path.map { p ->
                            Offset(p.x * scale, p.y * scale)
                        },
                        color = currentPath.color,
                        thickness = currentPath.thickness * scale
                    )
                }





            }
        }
    }


}