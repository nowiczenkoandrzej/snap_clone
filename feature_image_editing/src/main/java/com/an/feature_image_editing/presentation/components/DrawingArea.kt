package com.an.feature_image_editing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.feature_image_editing.presentation.util.PathData
import com.an.feature_image_editing.presentation.util.drawPencil

@Composable
fun DrawingArea(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    alpha: Float = 1f,
    paths: List<PathData>,
    currentPath: PathData,
    onDrawPath: (Offset) -> Unit,
    onFinishDrawingPath: () -> Unit
) {

    BoxWithConstraints(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxH = constraints.maxHeight.toFloat()
        val maxW = constraints.maxWidth.toFloat()
        val bitmapRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

        var targetHeight: Float
        var targetWidth: Float
        if (maxW / bitmapRatio <= maxH) {
            targetWidth = maxW
            targetHeight = maxW / bitmapRatio
        } else {
            targetHeight = maxH
            targetWidth = maxH * bitmapRatio
        }
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
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            onDrawPath(change.position)
                        },
                        onDragEnd = {
                            onFinishDrawingPath()
                        }
                    )
                }
        ) {

            paths.forEach { path ->
                drawPencil(
                    path = path.path,
                    color = path.color,
                    thickness = path.thickness
                )
            }

            drawPencil(
                path = currentPath.path,
                color = currentPath.color,
                thickness = currentPath.thickness
            )



        }
    }

}