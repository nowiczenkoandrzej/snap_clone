package com.an.feature_stickers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Result
import com.an.core_editor.domain.model.handle
import com.an.feature_stickers.domain.StickerCategory
import com.an.feature_stickers.domain.use_cases.StickersUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
                is StickerAction.LoadStickersByCategory -> {



                   /* useCases.loadStickerByCategory(action.category).handle(
                        onFailure = { message ->
                            _events.send(StickerEvent.ShowSnackbar(message))
                        },
                        onSuccess = { stickers ->
                            _stickerState.update { it.copy(
                                stickers = stickers,
                                selectedCategoryIndex = action.index
                            ) }
                        }
                    )*/
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
            }
        }
    }


}