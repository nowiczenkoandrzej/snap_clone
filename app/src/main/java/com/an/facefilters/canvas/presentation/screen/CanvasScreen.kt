package com.an.facefilters.canvas.presentation.screen

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.navigation.NavController
import com.an.facefilters.canvas.presentation.CanvasEvent
import com.an.facefilters.canvas.presentation.EditingAction
import com.an.facefilters.canvas.presentation.ElementAction
import com.an.facefilters.canvas.presentation.StickerAction
import com.an.facefilters.canvas.presentation.UiAction
import com.an.facefilters.canvas.domain.model.CanvasMode
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PanelMode.*
import com.an.facefilters.canvas.presentation.components.BottomActionsPanel
import com.an.facefilters.canvas.presentation.components.ColorPicker
import com.an.facefilters.canvas.presentation.components.ElementDrawer
import com.an.facefilters.canvas.presentation.components.TextInput
import com.an.facefilters.canvas.presentation.components.panels.ElementsPanel
import com.an.facefilters.canvas.presentation.components.ToolsSelector
import com.an.facefilters.canvas.presentation.components.panels.AspectRatioPanel
import com.an.facefilters.canvas.presentation.components.panels.FiltersPanel
import com.an.facefilters.canvas.presentation.components.panels.ImgPanel
import com.an.facefilters.canvas.presentation.components.panels.TextPanel
import com.an.facefilters.canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.canvas.presentation.util.pickImageFromGalleryLauncher
import com.an.facefilters.core.Screen
import com.an.facefilters.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    val elementsState by viewModel
        .elementsState
        .collectAsState()

    val uiState by viewModel
        .uiState
        .collectAsState()

    val pickImageLauncher = pickImageFromGalleryLauncher { bitmap ->
        viewModel.onAction(ElementAction.AddImage(bitmap = bitmap))
    }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                CanvasEvent.PickImage -> pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                CanvasEvent.NavigateToCropScreen -> navController.navigate(Screen.CropImage.route)
                CanvasEvent.NavigateToCreateStickerScreen -> navController.navigate(Screen.CreateSticker.route)
                is CanvasEvent.NavigateToStickersScreen -> navController.navigate(Screen.Stickers.route)
                is CanvasEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
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
            uiState.showColorPicker -> viewModel.onAction(UiAction.HideColorPicker)
            uiState.showToolsSelector -> viewModel.onAction(UiAction.HideToolsSelector)
            uiState.showTextInput -> viewModel.onAction(UiAction.HideTextInput)
            uiState.selectedPanelMode == FILTERS -> viewModel.onAction(UiAction.SetPanelMode(IMAGE))
            else -> {}
        }
    }
    val textMeasurer = rememberTextMeasurer()



    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.weight(5f)
                ) {

                    when(uiState.selectedCanvasMode) {

                        CanvasMode.CROP -> CropScreen(
                            originalBitmap = (elementsState.elements[elementsState.selectedElementIndex!!] as Img).bitmap,
                            onCropImage = { cropRect, imageSize ->
                                viewModel.onAction(EditingAction.CropImage(cropRect, imageSize))
                            }
                        )

                        CanvasMode.PENCIL -> {

                        }
                        CanvasMode.CREATE_STICKER -> CreateStickerScreen(
                            originalBitmap = (elementsState.elements[elementsState.selectedElementIndex!!] as Img).bitmap,
                            onFinish = { bitmap ->
                                viewModel.onAction(StickerAction.CreateSticker(bitmap))
                            }
                        )
                        CanvasMode.RUBBER -> {

                        }

                        CanvasMode.DEFAULT -> Canvas(
                            modifier = Modifier
                                .aspectRatio(uiState.aspectRatio)
                                .background(MaterialTheme.colorScheme.outline)
                                .padding(MaterialTheme.spacing.small)
                                .background(Color.White)
                                .pointerInput(uiState.selectedPanelMode) {
                                    detectTransformGesturesWithCallbacks(
                                        onGestureStart = {
                                            //viewModel.onAction(EditingAction.TransformStart)
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
                        ) {
                            ElementDrawer(
                                drawScope = this,
                                textMeasurer = textMeasurer,
                                elements = elementsState.elements,
                                context = context
                            )
                        }
                    }
                }




                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface),

                    ) {

                    when(uiState.selectedPanelMode) {
                        ELEMENTS -> {
                            ElementsPanel(
                                elements = elementsState.elements,
                                selectedElementIndex = elementsState.selectedElementIndex,
                                onDragAndDrop = { from, to ->
                                    viewModel.onAction(ElementAction.UpdateElementOrder(from, to))
                                },
                                onElementClick = { index ->
                                    viewModel.onAction(ElementAction.SelectElement(index))
                                },
                                onToolSelected = { toolType ->
                                    viewModel.onAction(UiAction.SelectTool(toolType))
                                }
                            )

                        }
                        PENCIL -> {
                            /*DrawingPanel(
                                modifier = Modifier.fillMaxWidth(),
                                onChangeThickness = { thickness ->
                                    viewModel.onAction(DrawingAction.SelectThickness(thickness))
                                },
                                onShowColorPicker = {
                                    viewModel.onAction(UiAction.ShowColorPicker)
                                },
                                selectedColor = state.selectedColor,
                                thickness = state.pathThickness
                            )*/
                        }

                        TEXT -> {
                            TextPanel(
                                modifier = Modifier.fillMaxWidth(),
                                selectedColor = uiState.selectedColor,
                                fontFamily = uiState.selectedFontFamily,
                                onShowColorPicker = {
                                    viewModel.onAction(UiAction.ShowColorPicker)
                                },
                                onChangeFont = { fontFamily ->
                                    viewModel.onAction(ElementAction.SelectFontFamily(fontFamily))
                                }
                            )
                        }

                        IMAGE -> {
                            ImgPanel(
                                onToolSelected =  { type ->
                                    viewModel.onAction(UiAction.SelectTool(type))
                                }
                            )

                        }
                        ASPECT_RATIO -> {
                            AspectRatioPanel(
                                onAspectRatioSelected = { aspectRatio ->
                                    viewModel.onAction(UiAction.SelectAspectRatio(aspectRatio))
                                },
                                selectedRatio = uiState.aspectRatio
                            )
                        }

                        FILTERS -> {

                            val selectedElement = elementsState.elements[elementsState.selectedElementIndex!!] as Img

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
                        onElementsClick = { viewModel.onAction(UiAction.SetPanelMode(ELEMENTS)) },
                        onToolsClick = { viewModel.onAction(UiAction.ShowToolsSelector) },
                        onUndo = {
                            //viewModel.onAction(ToolAction.Undo)
                        },
                    )

                }
            }

            AnimatedVisibility(
                visible = uiState.showToolsSelector,
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
                        .background(MaterialTheme.colorScheme.surface),
                    onToolSelected = { toolType ->
                        viewModel.onAction(UiAction.SelectTool(toolType))
                    },
                    onHidePanel = {
                        viewModel.onAction(UiAction.HideToolsSelector)
                    }
                )
            }

            if(uiState.showColorPicker) {
                ColorPicker(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.medium),
                    onColorSelected = { color ->
                        viewModel.onAction(UiAction.SelectColor(color))
                    }
                )
            }

            if(uiState.showTextInput) {
                TextInput(
                    onDismiss = { viewModel.onAction(UiAction.HideTextInput)},
                    onConfirm = { text ->
                        viewModel.onAction(ElementAction.AddText(text))
                    },
                    selectedColor = uiState.selectedColor
                )
            }


        }
    }


}



