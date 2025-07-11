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
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.core.graphics.scale
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ElementAction
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
import com.an.facefilters.canvas.presentation.components.panels.FiltersPanel
import com.an.facefilters.canvas.presentation.components.panels.ImgPanel
import com.an.facefilters.canvas.presentation.components.panels.TextPanel
import com.an.facefilters.canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.core.Screen
import com.an.facefilters.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val elementsState by viewModel
        .elementsState
        .collectAsState()

    val uiState by viewModel
        .uiState
        .collectAsState()



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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                CanvasEvent.PickImage -> pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                CanvasEvent.NavigateToCropScreen -> {
                    navController.navigate(Screen.CropImage.route)
                }
                CanvasEvent.NavigateToCreateStickerScreen -> {
                    navController.navigate(Screen.CreateSticker.route)
                }
                is CanvasEvent.NavigateToStickersScreen -> {
                    navController.navigate(Screen.Stickers.route)
                }
                is CanvasEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
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
            uiState.selectedMode == Mode.FILTERS -> {}//viewModel.onAction(ToolAction.SetMode(Mode.IMAGE))
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


                Canvas(
                    modifier = Modifier
                        .weight(5f)
                        .aspectRatio(uiState.aspectRatio)
                        .background(MaterialTheme.colorScheme.outline)
                        .padding(MaterialTheme.spacing.small)
                        .background(Color.White)
                        .pointerInput(uiState.selectedMode) {

                            when (uiState.selectedMode) {
                                Mode.PENCIL -> {

                                }

                                else -> {
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
                            }


                        }
                ) {
                    ElementDrawer(
                        drawScope = this,
                        textMeasurer = textMeasurer,
                        elements = elementsState.elements,
                        context = context
                    )

                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface),

                    ) {

                    when(uiState.selectedMode) {
                        Mode.ELEMENTS -> {
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
                        Mode.PENCIL -> {
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

                        Mode.TEXT -> {
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

                        Mode.IMAGE -> {
                            ImgPanel(
                                onToolSelected =  { type ->
                                    viewModel.onAction(UiAction.SelectTool(type))
                                }
                            )

                        }
                        Mode.ASPECT_RATIO -> {
                            AspectRatioPanel(
                                onAspectRatioSelected = { aspectRatio ->
                                    viewModel.onAction(UiAction.SelectAspectRatio(aspectRatio))
                                },
                                selectedRatio = uiState.aspectRatio
                            )
                        }

                        Mode.FILTERS -> {

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
                        onElementsClick = { viewModel.onAction(UiAction.SetMode(Mode.ELEMENTS)) },
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



