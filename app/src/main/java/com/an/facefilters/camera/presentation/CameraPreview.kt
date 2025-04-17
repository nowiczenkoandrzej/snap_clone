package com.an.facefilters.camera.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.an.facefilters.camera.domain.CameraScreenState
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
    state: CameraScreenState,
) {

    Box(
        modifier = modifier
    ) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    this.controller = controller
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = modifier
        )
        Canvas(
            modifier = modifier
        ) {
            FaceDrawer(
                face = state.face,
                scale = state.scale,
                delta = state.delta,
                scope = this
            )
        }
    }





}