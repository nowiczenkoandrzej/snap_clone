package com.an.facefilters.camera.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.camera.data.FaceDetector
import com.an.facefilters.camera.domain.CameraScreenAction
import com.an.facefilters.camera.domain.CameraScreenEvent
import com.an.facefilters.camera.domain.CameraScreenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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

            }
            CameraScreenAction.SwitchCamera -> {
                viewModelScope.launch {
                    if(screenState.value.isFrontCameraActive) {
                        _event.send(CameraScreenEvent.SwitchToBackCamera)
                    } else {
                        _event.send(CameraScreenEvent.SwitchToFrontCamera)
                    }
                    _screenState.value = screenState.value.copy(
                        isFrontCameraActive = !screenState.value.isFrontCameraActive
                    )
                }
            }

            is CameraScreenAction.ProcessImage -> {
                faceDetector.processImageProxy(
                    imageProxy = action.imageProxy,
                    onFaceDetected = { face ->
                        _screenState.value = screenState.value.copy(
                            face = face
                        )
                    }
                )
            }
        }
    }

}