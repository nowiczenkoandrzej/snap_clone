package com.an.feature_canvas.presentation

import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.an.core_editor.presentation.mappers.toPoint
import com.an.core_ui.ui.theme.spacing
import com.an.feature_canvas.presentation.components.AspectRatioPanel
import com.an.feature_canvas.presentation.components.BottomActionBar
import com.an.feature_canvas.presentation.components.ElementsPanel
import com.an.feature_canvas.presentation.components.ToolsSelector
import com.an.feature_canvas.presentation.util.PanelMode
import com.an.feature_canvas.presentation.util.ToolType
import com.an.feature_canvas.presentation.util.detectTransformGesturesWithCallbacks
import com.an.feature_canvas.presentation.util.elementDrawer
import com.an.feature_canvas.presentation.util.pickImageFromGalleryLauncher
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navigateToAddTextScreen: () -> Unit,
    navigateToStickerScreen: () -> Unit,
    navigateToEditTextScreen: () -> Unit,
    navigateToEditImageScreen: () -> Unit,
) {

    val editorState = viewModel
        .editorState
        .collectAsState()
        .value

    val uiState = viewModel
        .uiState
        .collectAsState()
        .value

    val density = LocalDensity.current
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val pickImageLauncher = pickImageFromGalleryLauncher(
        density = density
    ) { uri, screenWidth, screenHeight, padding ->
        viewModel.onAction(EditorAction.AddImage(
            uri = uri,
            screenPadding = padding,
            screenWidth = screenWidth.toFloat(),
            screenHeight = screenHeight.toFloat()
        ))
    }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is CanvasEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
                CanvasEvent.PickImageFromGallery -> pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
                CanvasEvent.NavigateToStickersScreen -> navigateToStickerScreen()
                CanvasEvent.NavigateToEditImageScreen -> navigateToEditImageScreen()
                CanvasEvent.NavigateToEditImageScreen -> navigateToEditTextScreen()
                CanvasEvent.NavigateToAddTextScreen -> navigateToAddTextScreen()
                is CanvasEvent.AddImageFromSavedProject -> {

                    val padding = with(density) { 8.dp.toPx() * 2 }
                    val displayMetrics = context.resources.displayMetrics


                    /*viewModel.onAction(EditorAction.AddImageFromSavedProject(
                        path = event.imagePath,
                        screenPadding = padding,
                        screenWidth = displayMetrics.widthPixels.toFloat(),
                        screenHeight = displayMetrics.heightPixels.toFloat()
                    ))*/
                }
                else -> {}
            }
        }
    }


    val localPadding = with(LocalDensity.current) { MaterialTheme.spacing.small.toPx() }

    val textMeasurer = rememberTextMeasurer()

    BackHandler {
        when {
            uiState.showToolsSelector -> viewModel.onAction(UiAction.ToggleToolSelector)
        }
    }

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

                    Canvas(
                        modifier = Modifier
                            .aspectRatio(uiState.aspectRatio)
                            .background(MaterialTheme.colorScheme.outline)
                            .padding(MaterialTheme.spacing.small)
                            .background(Color.White)
                            .onSizeChanged { canvasSize = it }
                            .pointerInput(Unit) {
                                detectTransformGesturesWithCallbacks(
                                    onGestureStart = {
                                        viewModel.onAction(EditorAction.TransformStart)
                                    },
                                    onGesture = { centroid, pan, zoom, rotation ->
                                        viewModel.onAction(
                                            EditorAction.TransformElement(
                                                scaleDelta = zoom,
                                                rotationDelta = rotation,
                                                translation = pan.toPoint()
                                            )
                                        )
                                    },
                                    onGestureEnd = {
                                        viewModel.onAction(EditorAction.TransformEnd)
                                    }
                                )
                            }.onSizeChanged { size ->
                                val paddingPx = localPadding
                                val innerWidth = size.width - (paddingPx * 2)
                                val innerHeight = size.height - (paddingPx * 2)
                                viewModel.onAction(UiAction.SetSize(size))
                            }
                    ) {

                        elementDrawer(
                            textMeasurer = textMeasurer,
                            elements = editorState.elements,
                            selectedElementIndex = editorState.selectedElementIndex,
                            showElementDetails = uiState.showElementDetail,
                            context = context
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface),

                ) {
                    when(uiState.panelMode) {
                        PanelMode.ASPECT_RATIO -> AnimatedVisibility(
                            visible = uiState.panelMode == PanelMode.ASPECT_RATIO,
                            enter = slideInVertically(
                                initialOffsetY = { it }
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { it }
                            )
                        ) {
                            AspectRatioPanel(
                                modifier = Modifier,
                                selectedRatio = uiState.aspectRatio,
                                onAspectRatioSelected = { newAspectRatio ->
                                    viewModel.onAction(UiAction.SelectAspectRatio(newAspectRatio))
                                }
                            )
                        }

                        PanelMode.ELEMENTS -> AnimatedVisibility(
                            visible = uiState.panelMode == PanelMode.ELEMENTS,
                            enter = slideInVertically(
                                initialOffsetY = { it }
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { it }
                            )
                        ) {
                            ElementsPanel(
                                elements = editorState.elements,
                                selectedElementIndex = editorState.selectedElementIndex,
                                onDragAndDrop = { from, to ->
                                    viewModel.onAction(
                                        EditorAction.ReorderElements(
                                            fromIndex = from,
                                            toIndex = to
                                        )
                                    )
                                },
                                onSelectElement = { index ->
                                    viewModel.onAction(EditorAction.SelectElement(index))
                                },
                                onDeleteElement = { index ->
                                    viewModel.onAction(EditorAction.DeleteElement(index))
                                },
                                onEditElement = {
                                    viewModel.onAction(EditorAction.NavigateToEditingScreen)
                                },
                                onAddImage = {
                                    viewModel.onAction(UiAction.SelectTool(ToolType.PICK_IMAGE_FROM_GALLERY))
                                },
                                onAddText = {
                                    viewModel.onAction(UiAction.SelectTool(ToolType.ADD_TEXT))
                                }
                            )
                        }

                    }
                }

                BottomActionBar(
                    modifier = Modifier.fillMaxWidth(),
                    onShowToolSelector = {
                        viewModel.onAction(UiAction.ToggleToolSelector)
                    },
                    onUndo = {
                        viewModel.onAction(EditorAction.Undo)
                    },
                    onShowElementPanel = {
                        viewModel.onAction(UiAction.ShowElementsPanel)
                    }
                )
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
                        viewModel.onAction(UiAction.ToggleToolSelector)
                    }
                )
            }
        }
    }



}