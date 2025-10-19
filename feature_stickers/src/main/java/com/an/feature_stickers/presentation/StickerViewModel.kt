package com.an.feature_stickers.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.handle
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.toPointList
import com.an.core_editor.presentation.toUiImageModel
import com.an.feature_stickers.domain.use_cases.StickersUseCases
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
    private val renderer: ImageRenderer
): ViewModel() {

    val editedImageModel: StateFlow<UiImageModel?> =
        editorRepository.state
            .map { state ->
                state.selectedElementIndex
                    ?.let { index -> state.elements.getOrNull(index) }
                    ?.let { element ->
                        if (element is DomainImageModel) {
                            element.toUiImageModel(renderer)
                        } else null
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _cuttingState = MutableStateFlow(CuttingState())
    val cuttingState = _cuttingState.asStateFlow()

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
                is StickerAction.CutBitmap -> {
                    if(_cuttingState.value.resultBitmap == null) {
                        editorRepository.getSelectedElement()?.let { model ->
                            Log.d("TAG", "onAction: sticker: $model")
                            if(model is DomainImageModel) {
                                useCases.cutImage(
                                    editedImage = model,
                                    selectedArea = _cuttingState.value.currentPath.toPointList()
                                ).handle(
                                    onSuccess = { cutBitmap ->
                                        _cuttingState.update { it.copy(
                                            resultBitmap = cutBitmap
                                        ) }

                                    },
                                    onFailure = { message ->
                                        _events.send(StickerEvent.ShowSnackbar(message))
                                    }
                                )
                            }
                        }
                    }



                }
                is StickerAction.AddSticker -> useCases
                    .addStickerToElements(
                        stickerPath = action.stickerPath,
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

                is StickerAction.UpdateCurrentPath -> _cuttingState.update { it.copy(
                    currentPath = it.currentPath + action.offset
                ) }


                StickerAction.ConfirmCutting -> {
                    editorRepository.getSelectedElement()?.let { model ->
                        Log.d("TAG", "onAction: sticker: $model")
                        if(model is DomainImageModel) {
                            useCases.saveCutting(
                                editedImage = model,
                                cuttingPath = PathData.DEFAULT.copy(
                                    path = cuttingState.value.currentPath.toPointList()
                                )
                            ).handle(
                                onSuccess = {
                                    _events.send(StickerEvent.PopBackStack)

                                },
                                onFailure = { message ->
                                    _events.send(StickerEvent.ShowSnackbar(message))
                                }
                            )
                        }
                    }
                    _events.send(StickerEvent.PopBackStack)
                }

                StickerAction.CancelCutting -> {
                    _cuttingState.update { it.copy(
                        resultBitmap = null,
                        currentPath = emptyList()
                    ) }
                }

                StickerAction.ToggleMagnifier -> {
                    _cuttingState.update { it.copy(
                        showMagnifier = !it.showMagnifier
                    ) }
                }
            }
        }
    }


}