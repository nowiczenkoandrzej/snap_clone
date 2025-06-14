package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.DrawingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.ToolAction
import com.an.facefilters.canvas.domain.UiAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.presentation.components.BottomActionsPanel
import com.an.facefilters.canvas.presentation.components.ColorPicker
import com.an.facefilters.canvas.presentation.components.TextInput
import com.an.facefilters.canvas.presentation.components.panels.ElementsPanel
import com.an.facefilters.canvas.presentation.components.ToolsSelector
import com.an.facefilters.canvas.presentation.components.panels.DrawingPanel
import com.an.facefilters.canvas.presentation.components.panels.TextPanel
import com.an.facefilters.canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.facefilters.canvas.presentation.util.drawPencil
import com.an.facefilters.core.Screen
import com.an.facefilters.ui.theme.spacing

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val state by viewModel
        .screenState
        .collectAsState()

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
            val originalBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))

            val displayMetrics = context.resources.displayMetrics

            val scale = minOf(
                displayMetrics.widthPixels.toFloat() / originalBitmap.width,
                displayMetrics.heightPixels.toFloat() / originalBitmap.height
            )

            val bitmap = originalBitmap.scale(
                (originalBitmap.width * scale).toInt(),
                (originalBitmap.height * scale).toInt()
            )

            viewModel.onAction(ElementAction.AddImage(bitmap = bitmap))
        }
    }

    LaunchedEffect(event) {
        when(event) {
            null -> {}
            CanvasEvent.PickImage -> {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                viewModel.onAction(UiAction.ConsumeEvent)
            }
            is CanvasEvent.ShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            CanvasEvent.NavigateToCropScreen -> {
                navController.navigate(Screen.CropImage.route)
                viewModel.onAction(UiAction.ConsumeEvent)
            }
            CanvasEvent.NavigateToCreateStickerScreen -> {
                navController.navigate(Screen.CreateSticker.route)
                viewModel.onAction(UiAction.ConsumeEvent)
            }
            else -> {

            }
        }
    }

    LaunchedEffect(Unit) {
        val bitmap = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Bitmap>("photo")

        if(bitmap != null) {
            viewModel.onAction(ElementAction.AddImage(bitmap))
        }
    }

    BackHandler {
        when {
            state.showColorPicker -> viewModel.onAction(UiAction.HideColorPicker)
            state.showToolsSelector -> viewModel.onAction(UiAction.HideToolsSelector)
            state.showTextInput -> viewModel.onAction(UiAction.HideTextInput)
            else -> viewModel.onAction(ToolAction.Undo)
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val textMeasurer = rememberTextMeasurer()

            Canvas(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .aspectRatio(state.aspectRatio)
                    .background(MaterialTheme.colorScheme.outline)
                    .padding(MaterialTheme.spacing.small)
                    .background(Color.White)
                    .pointerInput(state.selectedMode) {

                        when (state.selectedMode) {
                            Mode.PENCIL -> {
                                detectDragGestures(
                                    onDragStart = {
                                        viewModel.onAction(DrawingAction.StartDrawingPath)
                                    },
                                    onDrag = { change, _ ->
                                        viewModel.onAction(DrawingAction.DrawPath(change.position))
                                    },
                                    onDragEnd = {
                                        viewModel.onAction(DrawingAction.EndDrawingPath)
                                    }
                                )
                            }

                            else -> {
                                detectTransformGesturesWithCallbacks(
                                    onGestureStart = {
                                        viewModel.onAction(ElementAction.TransformStart)
                                    },
                                    onGesture = { centroid, pan, zoom, rotation ->
                                        viewModel.onAction(
                                            ElementAction.TransformElement(
                                                scale = zoom,
                                                rotation = rotation,
                                                offset = pan
                                            )
                                        )
                                    }
                                )

                            }
                        }


                    }
            ) {



                clipRect {

                    state.elements.forEachIndexed { index, layer ->
                        withTransform({
                            rotate(
                                degrees = layer.rotationAngle,
                                pivot = layer.pivot()
                            )
                            scale(
                                scale = layer.scale,
                                pivot = layer.pivot()
                            )
                        }) {

                            when(layer) {
                                is Img -> {
                                    drawImage(
                                        image = layer.bitmap.asImageBitmap(),
                                        topLeft = layer.p1,
                                        alpha = layer.alpha
                                    )
                                }
                                is TextModel -> {
                                    val layoutResult = textMeasurer.measure(
                                        text = AnnotatedString(layer.text),
                                        style = layer.textStyle
                                    )

                                    drawIntoCanvas { canvas ->
                                        canvas.save()
                                        canvas.translate(layer.p1.x, layer.p1.y)
                                        layoutResult.multiParagraph.paint(canvas)
                                        canvas.restore()
                                    }
                                }
                            }
                        }
                    }

                    state.paths.forEach { path ->
                        drawPencil(
                            path = path.path,
                            color = path.color,
                            thickness = path.thickness
                        )
                    }
                    state.drawnPath?.let { path ->
                        drawPencil(
                            path = path.path,
                            color = path.color,
                            thickness = path.thickness
                        )
                    }

                }

            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),

            ) {

                when(state.selectedMode) {
                    Mode.ELEMENTS -> {

                        val alpha = if(state.selectedElementIndex != null) {
                            state.elements[state.selectedElementIndex!!].alpha
                        } else {
                            1f
                        }



                        ElementsPanel(
                            elements = state.elements,
                            selectedElementIndex = state.selectedElementIndex,
                            alphaSliderPosition = alpha,
                            onDragAndDrop = { from, to ->
                                viewModel.onAction(ElementAction.SelectElement(from))
                                viewModel.onAction(ElementAction.DragAndDropElement(from, to))
                            },
                            onLayerClick = { index ->
                                viewModel.onAction(ElementAction.SelectElement(index))
                            },
                            onAlphaSliderChange = { position ->
                                viewModel.onAction(ElementAction.ChangeSliderPosition(position))
                            }
                        )

                    }
                    Mode.PENCIL -> {
                        DrawingPanel(
                            modifier = Modifier.fillMaxWidth(),
                            onChangeThickness = { thickness ->
                                viewModel.onAction(DrawingAction.SelectThickness(thickness))
                            },
                            onShowColorPicker = {
                                viewModel.onAction(UiAction.ShowColorPicker)
                            },
                            selectedColor = state.selectedColor,
                            thickness = state.pathThickness
                        )
                    }

                    Mode.TEXT -> {
                        TextPanel(
                            modifier = Modifier.fillMaxWidth(),
                            selectedColor = state.selectedColor,
                            fontFamily = state.selectedFontFamily,
                            onShowColorPicker = {
                                viewModel.onAction(UiAction.ShowColorPicker)
                            },
                            onChangeFont = { fontFamily ->
                                viewModel.onAction(ToolAction.SelectFontFamily(fontFamily))
                            }
                        )
                    }

                    Mode.IMAGE -> {

                    }
                }

                BottomActionsPanel(
                    modifier = Modifier.fillMaxSize(),
                    onElementsClick = { viewModel.onAction(ToolAction.SetMode(Mode.ELEMENTS)) },
                    onToolsClick = { viewModel.onAction(UiAction.ShowToolsSelector) },
                    onUndo = { viewModel.onAction(ToolAction.Undo) },
                    onRedo = { viewModel.onAction(ToolAction.Redo) }
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
                    .background(MaterialTheme.colorScheme.surface)
                    .pointerInput(state.showToolsSelector) {
                        detectVerticalDragGestures { change, dragAmount ->
                            if (state.showToolsSelector) {
                                viewModel.onAction(UiAction.HideToolsSelector)
                            }
                        }
                    },
                onToolSelected = { toolType ->
                    viewModel.onAction(ToolAction.SelectTool(toolType))
                },
                onHidePanel = {
                    viewModel.onAction(UiAction.HideToolsSelector)
                }
            )
        }

        if(state.showColorPicker) {
            ColorPicker(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(MaterialTheme.spacing.medium),
                onColorSelected = { color ->
                    viewModel.onAction(ToolAction.SelectColor(color))
                }
            )
        }

        if(state.showTextInput) {
            TextInput(
                onDismiss = { viewModel.onAction(UiAction.HideTextInput)},
                onConfirm = { text ->
                    viewModel.onAction(ToolAction.AddText(text))
                },
                selectedColor = state.selectedColor
            )
        }


    }

}



