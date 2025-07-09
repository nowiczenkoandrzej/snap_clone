package com.an.facefilters.canvas.presentation.vm

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.ElementsState
import com.an.facefilters.canvas.domain.StickerAction
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.domain.StickersState
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StickerViewModel(
    private val actionChannel: SendChannel<ElementAction>,
    private val stickerManager: StickerManager,
    private val subjectDetector: SubjectDetector
): ViewModel() {

    private val _stickersState = MutableStateFlow( StickersState(
        categories = stickerManager.getCategories(),
        stickers = stickerManager.loadStickers(StickerCategory.EMOJIS),
        userStickers = stickerManager.loadUserStickers()
    )
    )

    val stickerState = _stickersState.asStateFlow()

    fun onAction(action: StickerAction) {
        when(action) {
            is StickerAction.AddSticker -> addSticker(action.path)
            is StickerAction.CreateSticker -> createNewSticker(action.bitmap)
            is StickerAction.LoadStickers -> updateStickersList(action.category)
        }
    }

    private fun createNewSticker(bitmap: Bitmap) {
        subjectDetector.detectSubject(
            bitmap = bitmap,
            onSubjectDetected = { newSticker ->
                stickerManager.saveNewSticker(sticker = newSticker)
                viewModelScope.launch {
                    actionChannel.send(ElementAction.AddImage(newSticker))
                }
            },
            onError = {
                TODO()
            }
        )
    }

    private fun addSticker(path: String) {
        viewModelScope.launch {
            try {
                stickerManager.loadPngAsBitmap(path).also {
                    actionChannel.send(ElementAction.AddImage(it))

                    //sendEvent(CanvasEvent.StickerAdded)
                }
            } catch (e: Exception) {
                TODO()
            }
        }
    }

    private fun updateStickersList(category: StickerCategory) {
        _stickersState.update { it.copy(
            stickers = stickerManager.loadStickers(category),
            selectedCategory = category
        ) }
    }

    private inline fun updateState(block: StickersState.() -> StickersState) = _stickersState.update{ it.block() }



}