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

    private val categories = listOf(
        "Yours",
        "Activities",
        "Animals",
        "Clothing",
        "Emojis",
        "Food",
        "Music",
        "Objects"
    )

    init {
        viewModelScope.launch {

            val (categoriesResult, stickersResult, userStickersResult, nextResult, prevResult) = awaitAll(
                async { useCases.loadStickerCategories() },
                async { useCases.loadStickerByCategory(_stickerState.value.selectedCategory) },
                async { useCases.loadUserStickers() },
                async { useCases.loadStickerByCategory },
                async { useCases.loadUserStickers() }
            )

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
                is StickerAction.LoadStickersByCategory -> {



                    useCases.loadStickerByCategory(action.category).handle(
                        onFailure = { message ->
                            _events.send(StickerEvent.ShowSnackbar(message))
                        },
                        onSuccess = { stickers ->
                            _stickerState.update { it.copy(
                                stickers = stickers,
                                selectedCategoryIndex = action.index
                            ) }
                        }
                    )
                }
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