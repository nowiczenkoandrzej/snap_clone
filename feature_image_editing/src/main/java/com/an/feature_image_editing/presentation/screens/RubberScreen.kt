package com.an.feature_image_editing.presentation.screens

import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.core_editor.presentation.toOffsetList
import com.an.feature_image_editing.presentation.DrawingAction
import com.an.feature_image_editing.presentation.EditingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.RubberAction
import com.an.feature_image_editing.presentation.components.CheckerboardBackground
import com.an.feature_image_editing.presentation.util.drawPencil
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun RubberScreen(
    viewModel: ImageEditingViewModel,
    popBackStack: () -> Unit
) {

    val editedBitmap = viewModel
        .editedImageModel
        .collectAsState()
        .value
        ?.bitmap

    val state = viewModel
        .rubberState
        .collectAsState()
        .value

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

        CheckerboardBackground(
            modifier = Modifier
                .fillMaxSize()
        )
        editedBitmap?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val maxH = constraints.maxHeight.toFloat()
                    val maxW = constraints.maxWidth.toFloat()

                    val scale = min(
                        maxW / editedBitmap.width.toFloat(),
                        maxH / editedBitmap.height.toFloat()
                    )


                    var targetHeight = editedBitmap.height * scale
                    var targetWidth = editedBitmap.width * scale


                    val offsetX = (maxW - targetWidth) / 2f
                    val offsetY = (maxH - targetHeight) / 2f


                    val alpha = viewModel
                        .editedImageModel
                        .value
                        ?.alpha ?: 1f


                   /* AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.CENTER_INSIDE
                                adjustViewBounds = true
                                setImageBitmap(editedBitmap)
                            }
                        },
                        update = { imageView ->
                            imageView.setImageBitmap(editedBitmap)
                        },
                        modifier = Modifier
                            .width(targetWidth.dp)
                            .height(targetHeight.dp)
                            .graphicsLayer(alpha = alpha)


                    )*/

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
                                    onDragStart = {},
                                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                        val localX = (change.position.x) / scale
                                        val localY = (change.position.y) / scale
                                        viewModel.onAction(
                                            RubberAction.UpdateCurrentPath(
                                                Offset(
                                                    localX,
                                                    localY
                                                )
                                            )
                                        )
                                    },
                                    onDragEnd = {
                                        viewModel.onAction(RubberAction.AddNewPath)
                                    }
                                )
                            }
                    ){

                        drawImage(editedBitmap.asImageBitmap())

                        clipRect {

                            state.paths.forEach {

                                val path = Path().apply {
                                    moveTo(it.path.first().x, it.path.first().y)

                                    for (point in it.path) {
                                        lineTo(point.x, point.y)
                                    }
                                }

                                drawPath(
                                    path = path,
                                    color = Color.Transparent,
                                    style = Stroke(width = it.thickness),
                                    blendMode = BlendMode.Clear
                                )

                            }

                            drawPencil(
                                path = state.currentPath.path.toOffsetList(),
                                color = Color.White.copy(alpha = 0.5f),
                                thickness = state.currentPath.thickness
                            )

                        }

                    }
                }
            }
        }
    }


}