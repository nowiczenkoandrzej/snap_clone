package com.an.feature_stickers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result
import com.an.core_editor.domain.model.handle
import com.an.feature_stickers.domain.StickerCategory
import com.an.feature_stickers.domain.use_cases.StickersUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StickerViewModel(
    private val editorRepository: EditorRepository,
    private val useCases: StickersUseCases
): ViewModel() {

    val editorState = editorRepository.state

    private val _stickerState = MutableStateFlow(StickerState())
    val stickerState = _stickerState.asStateFlow()

    private val _events = Channel<StickerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {

            val categoriesDeferred = async { useCases.loadStickerCategories() }
            val stickersDeferred = async { useCases.loadStickerByCategory(_stickerState.value.selectedCategory) }
            val userStickersDeferred = async { useCases.loadUserStickers() }

            val categoriesResult = categoriesDeferred.await()
            val stickersResult = stickersDeferred.await()
            val userStickersResult = userStickersDeferred.await()

            val categories = (categoriesResult as? Result.Success)?.data ?: emptyList()
            val stickers = (stickersResult as? Result.Success)?.data ?: emptyList()
            val userStickers = (userStickersResult as? Result.Success)?.data ?: emptyList()

            _stickerState.update { it.copy(
                categories = categories,
                stickers = stickers,
                userStickers = userStickers
            ) }
        }
    }


    fun onAction(action: StickerAction) {
        viewModelScope.launch {
            when(action) {
                is StickerAction.CreateSticker -> {
                    useCases.createNewSticker(
                        imagePath = action.imagePath,
                        selectedArea = action.selectedArea
                    )
                }
                is StickerAction.LoadStickersByCategory -> useCases.loadStickerByCategory(action.category)
                is StickerAction.AddSticker -> useCases
                    .addStickerToElements(action.stickerPath)
                    .handle(
                        onSuccess = {
                            _events.send(StickerEvent.PopBackStack)
                        },
                        onFailure = { message ->
                            _events.send(StickerEvent.ShowSnackbar(message))
                        }
                    )
            }
        }
    }


}