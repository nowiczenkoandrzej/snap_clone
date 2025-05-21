package com.an.facefilters.canvas.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.model.Img

@Composable
fun CropScreen(
    navController: NavController,
    viewModel: CanvasViewModel
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(state.selectedLayerIndex != null) {
            val img = state.layers[state.selectedLayerIndex]

            if(img is Img) {
                Image(
                    bitmap = img.bitmap.asImageBitmap(),
                    contentDescription = null
                )
            }
        }

    }
}