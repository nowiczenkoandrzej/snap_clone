package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.presentation.util.isNear

@Composable
fun CropScreen(
    navController: NavController,
    viewModel: CanvasViewModel
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    val originalBitmap = (state.elements[state.selectedElementIndex!!] as Img).bitmap

    var cropRect by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val imagePadding = 16.dp
    val cornerHandleSize = 24.dp

    var corner by remember { mutableStateOf(SelectedCorner.NONE) }

    val context = LocalContext.current

    LaunchedEffect(event) {
        when(event) {
            CanvasEvent.ImageCropped -> navController.popBackStack()
            is CanvasEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(3f / 4f)
                .weight(4f)
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

            val primaryColor = MaterialTheme.colorScheme.primary
            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

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
                        color = onPrimaryColor,
                        center = corner,
                        radius = handleSize / 2
                    )
                    drawCircle(
                        color = primaryColor,
                        center = corner,
                        radius = handleSize / 3
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
        ) {

            Button(
                onClick = {
                    val newBitmap = originalBitmap.cropToRect(
                        srcRect = cropRect,
                        viewSize = imageSize
                    )
                    viewModel.onAction(ElementAction.CropImage(newBitmap))
                }
            ) {
                Text(stringResource(R.string.crop))
            }
        }


    }

}



fun Bitmap.cropToRect(srcRect: Rect, viewSize: IntSize): Bitmap {
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