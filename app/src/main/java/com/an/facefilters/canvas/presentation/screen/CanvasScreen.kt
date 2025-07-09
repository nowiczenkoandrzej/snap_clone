package com.an.facefilters.canvas.presentation.screen

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.core.graphics.scale
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.DrawingAction
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ToolAction
import com.an.facefilters.canvas.domain.UiAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.presentation.components.BottomActionsPanel
import com.an.facefilters.canvas.presentation.components.ColorPicker
import com.an.facefilters.canvas.presentation.components.ElementDrawer
import com.an.facefilters.canvas.presentation.components.TextInput
import com.an.facefilters.canvas.presentation.components.panels.ElementsPanel
import com.an.facefilters.canvas.presentation.components.ToolsSelector
import com.an.facefilters.canvas.presentation.components.panels.AspectRatioPanel
import com.an.facefilters.canvas.presentation.components.panels.DrawingPanel
import com.an.facefilters.canvas.presentation.components.panels.FiltersPanel
import com.an.facefilters.canvas.presentation.components.panels.ImgPanel
import com.an.facefilters.canvas.presentation.components.panels.TextPanel
import com.an.facefilters.canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.facefilters.canvas.presentation.vm.CanvasViewModel
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

            viewModel.onAction(EditingAction.AddImage(bitmap = bitmap))
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
            is CanvasEvent.NavigateToStickersScreen -> {
                navController.navigate(Screen.Stickers.route)
            }
            else -> {}

        }
    }

    LaunchedEffect(Unit) {
        val bitmap = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Bitmap>("photo")

        if(bitmap != null) {
            viewModel.onAction(EditingAction.AddImage(bitmap))
        }
    }

    BackHandler {
        when {
            state.showColorPicker -> viewModel.onAction(UiAction.HideColorPicker)
            state.showToolsSelector -> viewModel.onAction(UiAction.HideToolsSelector)
            state.showTextInput -> viewModel.onAction(UiAction.HideTextInput)
            state.selectedMode == Mode.FILTERS -> viewModel.onAction(ToolAction.SetMode(Mode.IMAGE))
            else -> {}
        }

    }
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Canvas(
                modifier = Modifier
                    .weight(5f)
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
                                        viewModel.onAction(EditingAction.TransformStart)
                                    },
                                    onGesture = { centroid, pan, zoom, rotation ->
                                        viewModel.onAction(
                                            EditingAction.TransformElement(
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
                ElementDrawer(
                    drawScope = this,
                    textMeasurer = textMeasurer,
                    elements = state.elements,
                    paths = state.paths,
                    currentPath = state.drawnPath,
                    context = context
                )

            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),

            ) {

                when(state.selectedMode) {
                    Mode.ELEMENTS -> {
                        ElementsPanel(
                            elements = state.elements,
                            selectedElementIndex = state.selectedElementIndex,
                            onDragAndDrop = { from, to ->
                                viewModel.onAction(EditingAction.UpdateElementOrder(from, to))
                            },
                            onElementClick = { index ->
                                viewModel.onAction(EditingAction.SelectElement(index))
                            },
                            onToolSelected = { toolType ->
                                viewModel.onAction(ToolAction.SelectTool(toolType))
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
                        ImgPanel(
                            onToolSelected =  { type ->
                                viewModel.onAction(ToolAction.SelectTool(type))
                            }
                        )

                    }
                    Mode.ASPECT_RATIO -> {
                        AspectRatioPanel(
                            onAspectRatioSelected = { aspectRatio ->
                                viewModel.onAction(ToolAction.SelectAspectRatio(aspectRatio))
                            },
                            selectedRatio = state.aspectRatio
                        )
                    }

                    Mode.FILTERS -> {

                        val selectedElement = state.elements[state.selectedElementIndex!!] as Img

                        FiltersPanel(
                            onFilterSelected = { filter ->
                                viewModel.onAction(EditingAction.ApplyFilter(filter))
                            },
                            alpha = selectedElement.alpha,
                            onAlphaChanged = { newAlpha ->
                                viewModel.onAction(EditingAction.ChangeElementAlpha(newAlpha))
                            },
                            currentFilter = selectedElement.currentFilter
                        )

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
                    if(toolType is ToolType.Save) {
                        viewModel.onAction(ToolAction.Save(textMeasurer = textMeasurer))
                    } else {
                        viewModel.onAction(ToolAction.SelectTool(toolType))
                    }

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



