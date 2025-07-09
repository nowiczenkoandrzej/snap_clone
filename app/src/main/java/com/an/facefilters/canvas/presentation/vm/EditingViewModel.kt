package com.an.facefilters.canvas.presentation.vm


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.presentation.util.cropToRect
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch

class EditingViewModel(
    private val actionChannel: SendChannel<ElementAction>,
    private val subjectDetector: SubjectDetector
): ViewModel() {


    fun onAction(action: EditingAction) {
        when(action) {
            is EditingAction.ApplyFilter -> applyFilter(action.element, action.filter)
            is EditingAction.ChangeElementAlpha -> changeAlphaSlider(action.element, action.alpha)
            is EditingAction.CropImage -> cropImage(action.element, action.srcRect, action.viewSize)
            is EditingAction.TransformElement -> transformElement(action.element, action.scale, action.rotation, action.offset)
            is EditingAction.RemoveBackground -> removeBackground(action.element)
        }
    }

    private fun removeBackground(element: Img) {

        subjectDetector.detectSubject(
            bitmap = element.bitmap,
            onSubjectDetected = { bitmap ->

                element.copy(
                    bitmap = bitmap
                ).also {
                    viewModelScope.launch {
                        actionChannel.send(ElementAction.UpdateElement(it))
                    }
                }
            },
            onError = {
                TODO()
            }
        )

    }



    private fun transformElement(
        element: Element,
        scale: Float,
        rotation: Float,
        offset: Offset
    ) {

        element.transform(
            scale = scale,
            rotation = rotation,
            offset = offset
        ).also {
            viewModelScope.launch {
                actionChannel.send(ElementAction.UpdateElement(it))
            }
        }

    }

    private fun cropImage(
        element: Img,
        srcRect: Rect,
        viewSize: IntSize,
    ) {

        val croppedBitmap = element.bitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        val croppedOriginalBitmap = element.originalBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        element.copy(
            bitmap = croppedBitmap,
            originalBitmap = croppedOriginalBitmap
        ).also {
            viewModelScope.launch {
                actionChannel.send(ElementAction.UpdateElement(it))
            }
        }

        //sendEvent(CanvasEvent.ImageCropped)
    }

    private fun changeAlphaSlider(
        element: Element,
        alpha: Float
    ) {
        element.setAlpha(alpha).also {
            viewModelScope.launch {
                actionChannel.send(ElementAction.UpdateElement(it))
            }
        }

    }

    private fun applyFilter(
        element: Img,
        filter: PhotoFilter
    ) {

        element.copy(
            bitmap = filter.apply(element.originalBitmap),
            currentFilter = filter.name
        ).also {
            viewModelScope.launch {
                actionChannel.send(ElementAction.UpdateElement(it))
            }
        }
    }



    /*private fun updateElementsOrder(from: Int, to: Int) {

        val size = _elementsState.value.elements.size

        if(from >= size || from < 0 || to >= size || to < 0) return

        updateState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    add(to, removeAt(from))
                }
                .toList()
        )}
    }*/

    /*private fun deleteElement() {
        val selectedElement = _elementsState.value.selectedElement ?: return

        updateState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    remove(selectedElement)
                }
                .toList()
        ) }

    }*/

    /*private fun addImage(bitmap: Bitmap) {
        val newList = _elementsState.value.elements + Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )
        updateState { copy(
            elements = newList,
            selectedElement = newList.last()
        ) }
    }*/

    /*private fun updateElement(newElement: Element) {

        val selectedElement = _elementsState.value.selectedElement ?: return


        updateState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    set(
                        index = lastIndexOf(selectedElement),
                        element = newElement
                    )
                }
                .toList()
        ) }
    }*/



}