package com.an.feature_image_editing.presentation.screens

import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.feature_image_editing.presentation.EditingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.core_ui.components.CheckerboardBackground
import com.an.feature_image_editing.presentation.util.SelectedCorner
import com.an.feature_image_editing.presentation.util.isNear
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun CroppingScreen(
    viewModel: ImageEditingViewModel,
    popBackStack: () -> Unit
) {

    val editedImage = viewModel
        .editedImageModel
        .collectAsState()
        .value


    var cropRect by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val cornerHandleSize = 24.dp


    var corner by remember { mutableStateOf(SelectedCorner.NONE) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler {
        popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                EditingEvent.PopBackStack -> {
                    popBackStack()
                }
                is EditingEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            editedImage?.let { editedImage ->
                val bitmap = editedImage.bitmap

                if(bitmap != null) {

                    val imageModifier = Modifier
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height)
                        .padding(8.dp)
                        .onGloballyPositioned {
                            imageSize = it.size
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    when {
                                        offset.isNear(cropRect.topLeft) -> corner =
                                            SelectedCorner.TOP_LEFT

                                        offset.isNear(cropRect.topRight) -> corner =
                                            SelectedCorner.TOP_RIGHT

                                        offset.isNear(cropRect.bottomLeft) -> corner =
                                            SelectedCorner.BOTTOM_LEFT

                                        offset.isNear(cropRect.bottomRight) -> corner =
                                            SelectedCorner.BOTTOM_RIGHT

                                        else -> corner = SelectedCorner.NONE
                                    }
                                },
                                onDrag = { change, _ ->

                                    val newX = change.position.x
                                    val newY = change.position.y

                                    when (corner) {
                                        SelectedCorner.TOP_LEFT -> {
                                            if( newY > cropRect.bottom)
                                                corner = SelectedCorner.BOTTOM_LEFT
                                            else if(newX > cropRect.right)
                                                corner = SelectedCorner.TOP_RIGHT
                                            else if (newY > 0 &&
                                                newX > 0
                                            ) {

                                                cropRect = cropRect.copy(
                                                    top = newY,
                                                    left = newX
                                                )
                                            }
                                        }

                                        SelectedCorner.TOP_RIGHT -> {
                                            if(newY > cropRect.bottom)
                                                corner = SelectedCorner.BOTTOM_RIGHT
                                            else if(newX < cropRect.left)
                                                corner = SelectedCorner.TOP_LEFT
                                            else if (newY > 0 &&
                                                newX < imageSize.width
                                            ) {

                                                cropRect = cropRect.copy(
                                                    top = newY,
                                                    right = newX
                                                )
                                            }
                                        }

                                        SelectedCorner.BOTTOM_LEFT -> {
                                            if(newY < cropRect.top)
                                                corner = SelectedCorner.TOP_LEFT
                                            else if(newX > cropRect.right)
                                                corner = SelectedCorner.BOTTOM_RIGHT
                                            else if (newY < imageSize.height &&
                                                newX > 0
                                            ) {

                                                cropRect = cropRect.copy(
                                                    bottom = newY,
                                                    left = newX
                                                )
                                            }
                                        }

                                        SelectedCorner.BOTTOM_RIGHT -> {
                                            if(newY < cropRect.top)
                                                corner = SelectedCorner.TOP_RIGHT
                                            else if(newX < cropRect.left)
                                                corner = SelectedCorner.BOTTOM_LEFT
                                            else if (newY < imageSize.height &&
                                                newX < imageSize.width
                                            ) {

                                                cropRect = cropRect.copy(
                                                    bottom = newY,
                                                    right = newX
                                                )
                                            }
                                        }

                                        SelectedCorner.NONE -> {

                                            val delta = change.position - change.previousPosition

                                            Log.d("TAG", "CroppingScreen: $delta")
                                            when {
                                                cropRect.top <= 0f && delta.y < 0f ||
                                                cropRect.left <= 0f && delta.x < 0f ||
                                                cropRect.right >= imageSize.width.toFloat() && delta.x > 0 ||
                                                cropRect.bottom >= imageSize.height.toFloat() && delta.y > 0 -> {}

                                                else -> cropRect = cropRect.translate(delta)
                                            }


                                        }
                                    }
                                }
                            )
                        }
                    Column {

                        Box(
                            modifier = Modifier.weight(5f)
                        ) {

                            CheckerboardBackground(
                                modifier = Modifier.fillMaxSize()
                            )

                            BoxWithConstraints(
                                modifier = Modifier
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
                                        .graphicsLayer(alpha = editedImage.alpha)
                                        .padding(8.dp)


                                )
                                val primaryColor = MaterialTheme.colorScheme.primary
                                val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

                                Canvas(modifier = imageModifier
                                    .width(targetWidth.dp)
                                    .height(targetHeight.dp)
                                ) {
                                    val canvasWidth = size.width
                                    val canvasHeight = size.height

                                    if (cropRect.isEmpty) {
                                        val initialSize = minOf(canvasWidth, canvasHeight) * 0.7f
                                        val offsetX = (canvasWidth - initialSize) / 2
                                        val offsetY = (canvasHeight - initialSize) / 2
                                        cropRect = Rect(offsetX, offsetY, offsetX + initialSize, offsetY + initialSize)
                                    }

                                    clipRect {
                                        // Square
                                        drawRect(
                                            color = Color.White.copy(alpha = 0.3f),
                                            topLeft = cropRect.topLeft,
                                            size = cropRect.size
                                        )

                                        // Edges
                                        drawRect(
                                            color = Color.White,
                                            topLeft = cropRect.topLeft,
                                            size = cropRect.size,
                                            style = Stroke(width = 2.dp.toPx())
                                        )

                                        // Corners
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
                            }


                        }




                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                viewModel.onAction(EditingAction.CancelCropping)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = {
                                viewModel.onAction(EditingAction.CropImage(
                                    srcRect = cropRect,
                                    viewSize = imageSize
                                ))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }



            }

        }
    }

}