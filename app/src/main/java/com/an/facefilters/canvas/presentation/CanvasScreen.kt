package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.presentation.components.BottomActionsPanel
import com.an.facefilters.canvas.presentation.components.panels.LayersPanel
import com.an.facefilters.canvas.presentation.components.ToolsSelector
import com.an.facefilters.canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.facefilters.canvas.presentation.util.drawPath
import kotlin.math.abs

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
            viewModel.onAction(CanvasAction.AddImage(bitmap = bitmap))
        }
    }

    LaunchedEffect(event) {
        when(event) {
            CanvasEvent.PickImage -> {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                viewModel.onAction(CanvasAction.ConsumeEvent)
            }
            null -> {}
        }
    }

    LaunchedEffect(Unit) {
        val bitmap = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Bitmap>("photo")

        if(bitmap != null) {
            viewModel.onAction(CanvasAction.AddImage(bitmap))
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3 / 4F)
                    .pointerInput(state.selectedMode) {


                        when (state.selectedMode) {
                            Mode.PENCIL -> {
                                detectDragGestures(
                                    onDragStart = {
                                        viewModel.onAction(CanvasAction.StartDrawingPath)
                                    },
                                    onDrag = { change, _ ->
                                        viewModel.onAction(CanvasAction.DrawPath(change.position))
                                    },
                                    onDragEnd = {
                                        viewModel.onAction(CanvasAction.EndDrawingPath)
                                    }
                                )
                            }

                            else -> {
                                detectTransformGesturesWithCallbacks(
                                    onGestureStart = {
                                        viewModel.onAction(CanvasAction.TransformStart)
                                    },
                                    onGesture = { centroid, pan, zoom, rotation ->
                                        viewModel.onAction(
                                            CanvasAction.TransformLayer(
                                                scale = zoom,
                                                rotation = rotation,
                                                offset = pan
                                            )
                                        )
                                    },
                                    onGestureEnd = {

                                    }
                                )

                            }
                        }


                    }
            ) {
                clipRect {

                    state.layers.forEachIndexed { index, layer ->
                        val alpha = if(state.selectedLayerIndex == index) {
                            1f
                        } else {
                            state.alphaSliderPosition
                        }
                        withTransform({
                            rotate(layer.rotationAngle)
                            scale(layer.scale)
                        }) {
                            when(layer) {
                                is Img -> {
                                    drawImage(
                                        image = layer.bitmap.asImageBitmap(),
                                        topLeft = layer.p1,
                                        alpha = alpha
                                    )
                                }
                            }
                        }
                    }

                    state.paths.forEach { path ->
                        drawPath(
                            path = path.path,
                            color = path.color,
                            thickness = path.thickness
                        )
                    }
                    state.drawnPath?.let { path ->
                        drawPath(
                            path = path.path,
                            color = path.color,
                            thickness = path.thickness
                        )
                    }

                }

            }

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer),

            ) {


                LayersPanel(
                    layers = state.layers,
                    selectedLayerIndex = state.selectedLayerIndex,
                    alphaSliderPosition = state.alphaSliderPosition,
                    onDragAndDrop = { from, to ->
                        viewModel.onAction(CanvasAction.SelectLayer(from))
                        viewModel.onAction(CanvasAction.DragAndDropLayers(from, to))
                    },
                    onLayerClick = { index ->
                        viewModel.onAction(CanvasAction.SelectLayer(index))
                    },
                    onAlphaSliderChange = { position ->
                        viewModel.onAction(CanvasAction.ChangeSliderPosition(position))
                    }
                )

                BottomActionsPanel(
                    modifier = Modifier
                        .fillMaxSize(),
                    onLayersClick = {
                        viewModel.onAction(CanvasAction.SelectLayersMode)
                    },
                    onToolsClick = {
                        viewModel.onAction(CanvasAction.ShowToolsSelector)
                    },
                    onUndo = {
                        viewModel.onAction(CanvasAction.Undo)
                    },
                    onRedo = {
                        viewModel.onAction(CanvasAction.Redo)
                    }
                )

            }
        }

        AnimatedVisibility(
            visible = state.showToolsSelector,
            modifier = Modifier
                .align(Alignment.BottomStart) ,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            ToolsSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(state.showToolsSelector) {
                        detectVerticalDragGestures { change, dragAmount ->
                            if (state.showToolsSelector) {
                                viewModel.onAction(CanvasAction.HideToolsSelector)
                            }
                        }
                    },
                onToolSelected = { toolType ->
                    viewModel.onAction(CanvasAction.SelectTool(toolType))
                },
                onHidePanel = {
                    viewModel.onAction(CanvasAction.HideToolsSelector)
                }
            )
        }

    }

}



