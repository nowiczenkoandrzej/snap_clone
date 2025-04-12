package com.an.facefilters.camera.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.an.facefilters.camera.data.FaceDetector
import com.an.facefilters.camera.domain.CameraScreenAction
import com.an.facefilters.camera.domain.CameraScreenEvent
import com.an.facefilters.camera.domain.CameraScreenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CameraViewModel(
    private val faceDetector: FaceDetector
): ViewModel() {

    private val _screenState = MutableStateFlow(CameraScreenState())
    val screenState = _screenState.asStateFlow()

    private val _event = Channel<CameraScreenEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: CameraScreenAction) {
        when(action) {
            is CameraScreenAction.SetCameraSettings -> {

                val scale = maxOf(
                    (action.screenWidth.toFloat() / action.imageWidth),
                    (action.screenHeight.toFloat() / action.imageHeight)
                )

                _screenState.value = screenState.value.copy(
                    screenHeight = action.screenHeight,
                    screenWidth = action.screenWidth,
                    imageWidth = action.imageWidth,
                    imageHeight = action.imageHeight,
                    scale = scale,
                    delta = (((action.imageWidth * scale) - action.screenWidth) / 2)
                )

                Log.d("TAG", "onAction: ${screenState.value}")

            }
            CameraScreenAction.SwitchCamera -> {

            }

            is CameraScreenAction.ProcessImage -> {
                faceDetector.processImageProxy(
                    imageProxy = action.imageProxy,
                    onFaceDetected = { points ->
                        _screenState.value = screenState.value.copy(
                            contours = points
                        )
                    }
                )
            }
        }
    }

}