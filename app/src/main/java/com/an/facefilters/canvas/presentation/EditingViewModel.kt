package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ElementsState
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.domain.StickersState
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.presentation.util.cropToRect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditingViewModel(
    private val stickerManager: StickerManager,
    private val subjectDetector: SubjectDetector
): ViewModel() {
    private val _elementsState = MutableStateFlow(ElementsState())
    val elementsState = _elementsState.asStateFlow()

    private val _stickersState = MutableStateFlow(StickersState(
            categories = stickerManager.getCategories(),
            stickers = stickerManager.loadStickers(StickerCategory.EMOJIS),
            userStickers = stickerManager.loadUserStickers()
        )
    )

    val stickerState = _stickersState.asStateFlow()


    fun onAction(action: EditingAction) {
        when(action) {
            is EditingAction.AddImage -> addImage(action.bitmap)
            is EditingAction.ApplyFilter -> applyFilter(action.filter)
            is EditingAction.ChangeSliderPosition -> changeAlphaSlider(action.alpha)
            is EditingAction.CropImage -> cropImage(action.srcRect, action.viewSize)
            EditingAction.DeleteElement -> deleteElement()
            is EditingAction.UpdateElementOrder -> updateElementsOrder(action.fromIndex, action.toIndex)
            is EditingAction.SelectElement -> selectElement(action.index)
            is EditingAction.TransformElement -> transformElement(action.scale, action.rotation, action.offset)
            is EditingAction.AddSticker -> addSticker(action.path)
            is EditingAction.CreateSticker -> createNewSticker(action.bitmap)
            is EditingAction.LoadStickers -> updateStickersList(action.category)
            EditingAction.RemoveBackground -> removeBackground()
        }
    }

    private fun removeBackground() {
        _elementsState.value.selectedElement?.let { element ->
            if(element !is Img) {
                return
            }

            subjectDetector.detectSubject(
                bitmap = element.bitmap,
                onSubjectDetected = { bitmap ->
                    element.copy(
                        bitmap = bitmap
                    ).also { updateElement(it) }
                },
                onError = {}
            )
        }
    }

    private fun createNewSticker(bitmap: Bitmap) {
        subjectDetector.detectSubject(
            bitmap = bitmap,
            onSubjectDetected = { newSticker ->
                stickerManager.saveNewSticker(sticker = newSticker)
                addImage(newSticker)
            },
            onError = {
                TODO()
            }
        )
    }


    private fun updateStickersList(category: StickerCategory) {
        _stickersState.update { it.copy(
            stickers = stickerManager.loadStickers(category),
            selectedCategory = category
        ) }
    }

    private fun addSticker(path: String) {
        viewModelScope.launch {
            try {
                stickerManager.loadPngAsBitmap(path).also {
                    addImage(it)
                    //sendEvent(CanvasEvent.StickerAdded)
                }
            } catch (e: Exception) {
                TODO()
            }
        }
    }

    private fun transformElement(
        scale: Float,
        rotation: Float,
        offset: Offset
    ) {
        val selectedElement = _elementsState.value.selectedElement ?: return

        selectedElement.transform(
            scale = scale,
            rotation = rotation,
            offset = offset
        ).also { updateElement(it) }

    }

    private fun selectElement(index: Int) {

        if(index >= _elementsState.value.elements.size) return

        updateState { copy(
            selectedElement = _elementsState
                .value
                .elements[index]
        ) }
    }

    private fun updateElementsOrder(from: Int, to: Int) {

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
    }

    private fun deleteElement() {
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

    }

    private fun cropImage(srcRect: Rect, viewSize: IntSize) {

        val croppedImg = _elementsState.value.selectedElement ?: return

        if(croppedImg !is Img) return

        val croppedBitmap = croppedImg.bitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        val croppedOriginalBitmap = croppedImg.originalBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        croppedImg.copy(
            bitmap = croppedBitmap,
            originalBitmap = croppedOriginalBitmap
        ).also { updateElement(it) }

        //sendEvent(CanvasEvent.ImageCropped)
    }


    private fun changeAlphaSlider(alpha: Float) {
        _elementsState.value.selectedElement?.let {
            it.setAlpha(alpha).also { updateElement(it) }
        }
    }

    private fun addImage(bitmap: Bitmap) {
        val newList = _elementsState.value.elements + Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )
        updateState { copy(
            elements = newList,
            selectedElement = newList.last()
        ) }
    }
    private fun applyFilter(filter: PhotoFilter) {
        val selectedElement = _elementsState.value.selectedElement ?: return

        if(selectedElement !is Img) return

        selectedElement.copy(
            bitmap = filter.apply(selectedElement.originalBitmap),
            currentFilter = filter.name
        ).also { updateElement(it) }
    }

    private fun updateElement(newElement: Element) {

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
    }

    private inline fun updateState(block: ElementsState.() -> ElementsState) = _elementsState.update{ it.block() }


}