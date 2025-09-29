package com.an.feature_image_editing.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.toUiImageModel
import com.an.feature_image_editing.presentation.EditingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.components.ImagePreview
import com.an.feature_image_editing.presentation.util.SelectedCorner
import com.an.feature_image_editing.presentation.util.isNear
import kotlinx.coroutines.launch

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
                        .fillMaxWidth()
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height)
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

                                    when (corner) {
                                        SelectedCorner.TOP_LEFT -> {
                                            if (change.position.y > 0 &&
                                                change.position.x > 0
                                            ) {

                                                cropRect = cropRect.copy(
                                                    top = change.position.y,
                                                    left = change.position.x
                                                )
                                            }
                                        }

                                        SelectedCorner.TOP_RIGHT -> {
                                            if (change.position.y > 0 &&
                                                change.position.x < imageSize.width
                                            ) {

                                                cropRect = cropRect.copy(
                                                    top = change.position.y,
                                                    right = change.position.x
                                                )
                                            }
                                        }

                                        SelectedCorner.BOTTOM_LEFT -> {
                                            if (change.position.y < imageSize.height &&
                                                change.position.x > 0
                                            ) {

                                                cropRect = cropRect.copy(
                                                    bottom = change.position.y,
                                                    left = change.position.x
                                                )
                                            }
                                        }

                                        SelectedCorner.BOTTOM_RIGHT -> {
                                            if(change.position.y < imageSize.height &&
                                                change.position.x < imageSize.width) {

                                                cropRect = cropRect.copy(
                                                    bottom = change.position.y,
                                                    right = change.position.x
                                                )
                                            }
                                        }

                                        SelectedCorner.NONE -> {

                                            cropRect =
                                                cropRect.translate(change.position - change.previousPosition)
                                        }
                                    }
                                }
                            )
                        }
                    Column {

                        Box(
                            modifier = Modifier.weight(5f)
                        ) {
                            ImagePreview(
                                modifier = imageModifier.onGloballyPositioned {
                                    imageSize = it.size
                                },
                                bitmap = bitmap,
                                alpha = editedImage.alpha
                            )
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

                            Canvas(modifier = imageModifier) {
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




                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
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