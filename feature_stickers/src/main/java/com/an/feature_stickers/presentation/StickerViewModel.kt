package com.an.feature_stickers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.core_editor.domain.model.handle
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.toUiImageModel
import com.an.feature_stickers.domain.StickerCategory
import com.an.feature_stickers.domain.use_cases.StickersUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StickerViewModel(
    private val editorRepository: EditorRepository,
    private val useCases: StickersUseCases,
    private val bitmapCache: BitmapCache
): ViewModel() {

    val editedImageModel: StateFlow<UiImageModel?> =
        editorRepository.state
            .map { state ->
                state.selectedElementIndex
                    ?.let { index -> state.elements.getOrNull(index) }
                    ?.let { element ->
                        if (element is DomainImageModel) {
                            element.toUiImageModel(bitmapCache)
                        } else null
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _createStickerState = MutableStateFlow(CreateStickerState())
    val createStickerState = _createStickerState.asStateFlow()

    private val _stickerState = MutableStateFlow(StickerState())
    val stickerState = _stickerState.asStateFlow()

    private val _events = Channel<StickerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    init {
        viewModelScope.launch {
            useCases.loadStickersMap().handle(
                onSuccess = { map ->
                    _stickerState.update { it.copy(
                        stickersMap = map,
                        selectedCategoryIndex = 1
                    ) }
                },
                onFailure = { message ->
                    _events.send(StickerEvent.ShowSnackbar(message))
                }
            )

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
                is StickerAction.AddSticker -> useCases
                    .addStickerToElements(
                        stickerPath = action.stickerPath,
                        isFromAssets = action.isFromAssets
                    ).handle(
                        onSuccess = {
                            _events.send(StickerEvent.PopBackStack)
                        },
                        onFailure = { message ->
                            _events.send(StickerEvent.ShowSnackbar(message))
                        }
                    )

                is StickerAction.SelectCategory -> {
                    _stickerState.update { it.copy(
                        selectedCategoryIndex = action.index
                    ) }
                }

                is StickerAction.UpdateCurrentPath -> _createStickerState.update { it.copy(
                    currentPath = it.currentPath + action.offset
                ) }
                StickerAction.AddPath -> _createStickerState.update { it.copy(
                    paths = it.paths + listOf(it.currentPath),
                    currentPath = emptyList()
                ) }
            }
        }
    }


}