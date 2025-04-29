package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Tools
import com.an.facefilters.canvas.presentation.components.LayersPanel
import com.an.facefilters.canvas.presentation.components.ToolsBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if(state.showToolsBottomSheet) {
                ToolsBottomSheet(
                    sheetState = sheetState,
                    onToolSelected = { tool ->
                        viewModel.onAction(CanvasAction.SelectTool(tool))
                    },
                    onDismiss = {
                        viewModel.onAction(CanvasAction.HideToolsBottomSheet)
                    },
                    scope = scope
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3 / 4F)
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->

                            viewModel.onAction(
                                CanvasAction.TransformLayer(
                                    scale = zoom,
                                    rotation = rotation,
                                    offset = pan
                                )
                            )

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

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text("Layers")
                    }
                    TextButton(
                        onClick = {
                            viewModel.onAction(CanvasAction.ShowToolsBottomSheet)
                        }
                    ) {
                        Text("Tools")
                    }
                    IconButton(
                        onClick = {
                            viewModel.onAction(CanvasAction.SelectTool(Tools.AddPhoto))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }

            }
        }


    }

}

