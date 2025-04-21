package com.an.facefilters.canvas.presentation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Layer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.context.startKoin

class CanvasViewModel(

): ViewModel() {

    private val _screenState = MutableStateFlow(CanvasState())
    val screenState = _screenState.asStateFlow()

    fun onAction(action: CanvasAction) {
        when(action) {
            is CanvasAction.TransformLayer -> transformLayer(action)

            is CanvasAction.SelectLayer -> { selectLayer(action.offset) }

            is CanvasAction.InsertInitialBitmap -> {
                _screenState.update { it.copy(
                    layers = screenState.value.layers + Img(bitmap = action.bitmap)
                ) }
            }

            is CanvasAction.AddImage -> {
                _screenState.update { it.copy(
                    layers = screenState.value.layers + Img(bitmap = action.bitmap)
                ) }
            }
        }
    }

    private fun transformLayer(action: CanvasAction.TransformLayer) {
        if(screenState.value.selectedLayerIndex == null) return

        val updatedLayer = screenState
            .value
            .layers[screenState.value.selectedLayerIndex!!]
            .transform(
                scale = action.scale,
                rotation = action.rotation,
                offset = action.offset
            )
        updateLayer(updatedLayer)
    }




    private fun selectLayer(offset: Offset) {
        _screenState.value.layers.forEachIndexed { index, layer ->
            if(layer.containsTouchPoint(offset)) {
                _screenState.update { it.copy(
                    selectedLayerIndex = index
                ) }
            }

        }
    }

    private fun updateLayer(newLayer: Layer) {
        val newList = screenState.value.layers.toMutableList().apply {
            this[screenState.value.selectedLayerIndex!!] = newLayer
        }.toList()

        _screenState.update { it.copy(
            layers = newList
        ) }
    }


}