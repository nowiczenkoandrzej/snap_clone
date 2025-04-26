package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Tools
import com.an.facefilters.canvas.presentation.components.ToolsBottomSheet
import com.an.facefilters.ui.theme.spacing

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
            viewModel.onAction(CanvasAction.InsertInitialBitmap(bitmap))
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

                            val currentIndex = viewModel.screenState.value.selectedLayerIndex

                            if (currentIndex == null) {
                                viewModel.onAction(CanvasAction.SelectLayer(centroid))
                            } else {
                                viewModel.onAction(
                                    CanvasAction.TransformLayer(
                                        scale = zoom,
                                        rotation = rotation,
                                        offset = pan
                                    )
                                )
                            }
                        }
                    }
            ) {
                clipRect {

                    state.layers.forEach { layer ->
                        withTransform({
                            rotate(layer.rotationAngle)
                            scale(layer.scale)
                        }) {
                            when(layer) {
                                is Img -> {
                                    drawImage(
                                        image = layer.bitmap.asImageBitmap(),
                                        topLeft = layer.p1
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

                Text(
                    text = stringResource(R.string.layers),
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                )

                if(state.layers.isEmpty()) {
                    Text("No layers yet")
                } else {
                    LazyRow {
                        itemsIndexed(state.layers) { index, layer ->
                            Card(
                                modifier = Modifier
                                    .size(MaterialTheme.spacing.cardSmall)
                                    .padding(MaterialTheme.spacing.small)
                                    .background(color = Color.Red)
                                    .clickable {
                                        viewModel.onAction(CanvasAction.ChangeLayer(index))
                                    }
                            ) {

                            }
                        }
                    }

                }


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
