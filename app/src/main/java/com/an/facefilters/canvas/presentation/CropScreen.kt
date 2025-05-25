package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.model.Img
import java.lang.Math.pow
import kotlin.math.sqrt

@Composable
fun CropScreen(
    navController: NavController,
    viewModel: CanvasViewModel
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val originalBitmap = (state.layers[state.selectedLayerIndex!!] as Img).bitmap

    var cropRect by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val imagePadding = 16.dp
    val cornerHandleSize = 24.dp

    var corner by remember { mutableStateOf(SelectedCorner.NONE) }

    val imageModifier = Modifier
        .padding(imagePadding)
        .fillMaxWidth()
        .aspectRatio(originalBitmap.width.toFloat() / originalBitmap.height)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    when {
                        offset.isNear(cropRect.topLeft) -> corner = SelectedCorner.TOP_LEFT
                        offset.isNear(cropRect.topRight) -> corner = SelectedCorner.TOP_RIGHT
                        offset.isNear(cropRect.bottomLeft) -> corner = SelectedCorner.BOTTOM_LEFT
                        offset.isNear(cropRect.bottomRight) -> corner = SelectedCorner.BOTTOM_RIGHT
                        else -> corner = SelectedCorner.NONE
                    }
                },
                onDrag = { change, _ ->

                    when (corner) {
                        SelectedCorner.TOP_LEFT -> cropRect = cropRect.copy(
                            top = change.position.y,
                            left = change.position.x
                        )

                        SelectedCorner.TOP_RIGHT -> cropRect = cropRect.copy(
                            top = change.position.y,
                            right = change.position.x
                        )

                        SelectedCorner.BOTTOM_LEFT -> cropRect = cropRect.copy(
                            bottom = change.position.y,
                            left = change.position.x
                        )

                        SelectedCorner.BOTTOM_RIGHT -> cropRect = cropRect.copy(
                            bottom = change.position.y,
                            right = change.position.x
                        )

                        SelectedCorner.NONE -> {
                            cropRect = cropRect.translate(change.position - change.previousPosition)
                        }
                    }
                }
            )
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageBitmap(originalBitmap)
                }
            },
            modifier = imageModifier.onGloballyPositioned {
                imageSize = it.size
            }
        )

        Canvas(modifier = imageModifier) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Inicjalizacja cropRect jeśli jest pusty
            if (cropRect.isEmpty) {
                val initialSize = minOf(canvasWidth, canvasHeight) * 0.7f
                val offsetX = (canvasWidth - initialSize) / 2
                val offsetY = (canvasHeight - initialSize) / 2
                cropRect = Rect(offsetX, offsetY, offsetX + initialSize, offsetY + initialSize)
            }

            // Rysowanie prostokąta
            drawRect(
                color = Color.White.copy(alpha = 0.3f),
                topLeft = cropRect.topLeft,
                size = cropRect.size
            )

            // Rysowanie krawędzi
            drawRect(
                color = Color.White,
                topLeft = cropRect.topLeft,
                size = cropRect.size,
                style = Stroke(width = 2.dp.toPx())
            )

            // Rysowanie uchwytów w rogach
            val handleSize = cornerHandleSize.toPx()
            listOf(
                cropRect.topLeft,
                cropRect.topRight,
                cropRect.bottomLeft,
                cropRect.bottomRight
            ).forEach { corner ->
                drawCircle(
                    color = Color.White,
                    center = corner,
                    radius = handleSize / 2
                )
                drawCircle(
                    color = Color.Blue,
                    center = corner,
                    radius = handleSize / 3
                )
            }
        }
    }
}

private fun Offset.isNear(p1: Offset): Boolean {
    val distance = sqrt(pow((this.x - p1.x).toDouble(), 2.0) + pow((this.y - p1.y).toDouble(), 2.0))

    return distance < 50.0
}

private fun Bitmap.cropToRect(srcRect: Rect, viewSize: IntSize): Bitmap {
    val scaleX = width.toFloat() / viewSize.width
    val scaleY = height.toFloat() / viewSize.height

    val x = (srcRect.left * scaleX).toInt().coerceIn(0, width)
    val y = (srcRect.top * scaleY).toInt().coerceIn(0, height)
    val width = (srcRect.width * scaleX).toInt().coerceIn(0, width - x)
    val height = (srcRect.height * scaleY).toInt().coerceIn(0, height - y)

    return Bitmap.createBitmap(this, x, y, width, height)
}

enum class SelectedCorner{
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, NONE
}