package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.model.CanvasMode
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PanelMode
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.domain.use_cases.editing.EditingUseCases
import com.an.facefilters.canvas.domain.use_cases.elements.ElementsUseCases
import com.an.facefilters.canvas.domain.use_cases.stickers.StickersUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CanvasViewModel(
    private val elementsUseCases: ElementsUseCases,
    private val editingUseCases: EditingUseCases,
    private val stickerUseCases: StickersUseCases
): ViewModel() {

    private val _elementsState = MutableStateFlow(ElementsState())
    val elementsState = _elementsState.asStateFlow()

    private val ElementsState.selectedElement: Element?
        get() = selectedElementIndex?.let { elements.getOrNull(it) }

    private val _stickersState = MutableStateFlow(StickersState())
    val stickersState = _stickersState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<CanvasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        stickerUseCases.loadStickerState().also { result ->
            when(result) {
                is Result.Failure -> showError(result.message)
                is Result.Success<StickersState> -> _stickersState.value = result.data
            }
        }
    }

    fun onAction(action: CanvasAction) {
        when(action) {
            is EditingAction -> handleEditingAction(action)
            is StickerAction -> handleStickerAction(action)
            is ElementAction -> handleElementAction(action)
            is UiAction -> handleUiAction(action)
        }
    }

    private fun handleStickerAction(action: StickerAction) {
        when(action) {
            is StickerAction.AddSticker -> {
                stickerUseCases.loadSticker(action.path).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<Bitmap> -> addImage(result.data)
                    }
                }
                hideSelectors()

            }
            is StickerAction.CreateSticker -> {
                try {
                    stickerUseCases.createNewSticker(
                        action.bitmap,
                        onStickerCreated = { sticker ->
                            addImage(sticker)
                        }
                    )
                } catch (e: Exception) {
                    showError(e.message.toString())
                }
                hideSelectors()
                updateUi { copy(selectedCanvasMode = CanvasMode.DEFAULT) }

            }
            is StickerAction.LoadStickersByCategory -> {
                stickerUseCases.loadStickerByCategory(action.category).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<List<String>> -> {
                            _stickersState.update { it.copy(
                                stickers = result.data,
                                selectedCategory = action.category
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun handleEditingAction(action: EditingAction) {
        hideSelectors()
        when(action) {
            is EditingAction.ApplyFilter -> {

                editingUseCases.applyFilter(
                    element = _elementsState.value.selectedElement,
                    filter = action.filter
                ).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<Img> -> {
                            updateElement(result.data)
                        }
                    }
                }
            }
            is EditingAction.ChangeElementAlpha -> {
                editingUseCases.changeElementAlpha(
                    element = _elementsState.value.selectedElement,
                    alpha = action.alpha
                ).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<Element> -> updateElement(result.data)
                    }
                }
            }
            is EditingAction.CropImage -> {
                editingUseCases.cropImage(
                    element = _elementsState.value.selectedElement,
                    srcRect = action.srcRect,
                    viewSize = action.viewSize
                ).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<Img> -> updateElement(result.data)
                    }
                }
                updateUi { copy(selectedCanvasMode = CanvasMode.DEFAULT) }
            }
            is EditingAction.RemoveBackground -> {
                try {
                    editingUseCases.removeBackground(
                        element = _elementsState.value.selectedElement,
                        onDetect = { newImg ->
                            if(newImg != null)
                                updateElement(newImg)
                            else
                                showError("Something went wrong...")
                        }
                    )
                } catch (e: Exception) {
                    showError("Something went wrong...")
                }
            }
            is EditingAction.TransformElement -> {
                editingUseCases.transformElement(
                    element = _elementsState.value.selectedElement,
                    scale = action.scale,
                    rotation = action.rotation,
                    offset = action.offset
                ).also { result ->
                    when(result) {
                        is Result.Failure -> {}
                        is Result.Success<Element> -> updateElement(result.data)
                    }
                }
            }
        }
    }

    private fun handleElementAction(action: ElementAction) {
        when(action) {
            is ElementAction.AddImage -> addImage(action.bitmap)
            ElementAction.DeleteElement -> {
                elementsUseCases.deleteElement(
                    list = _elementsState.value.elements,
                    element = _elementsState.value.selectedElement
                ).also { result ->
                    when(result) {
                        is Result.Failure -> showError(result.message)
                        is Result.Success<List<Element>> -> _elementsState.update { it.copy(
                            elements = result.data
                        ) }
                    }
                }
            }
            is ElementAction.SelectElement -> selectElement(action.index)
            is ElementAction.UpdateElement -> updateElement(action.element)
            is ElementAction.UpdateElementOrder -> elementsUseCases.updateElementsOrder(
                from = action.fromIndex,
                to = action.toIndex,
                list = _elementsState.value.elements,
            ).also { result ->
                when(result) {
                    is Result.Failure -> showError(result.message)
                    is Result.Success<List<Element>> -> {
                        _elementsState.update { it.copy(elements = result.data) }
                    }
                }
            }
            is ElementAction.AddText -> elementsUseCases.addText(
                text = action.text,
                elements = _elementsState.value.elements,
                fontFamily = _uiState.value.selectedFontFamily,
                color = _uiState.value.selectedColor
            ).also { newList ->
                _elementsState.update { it.copy(
                    elements = newList,
                    selectedElementIndex = newList.size
                ) }
                hideSelectors()

            }
            is ElementAction.SelectFontFamily -> {
                elementsUseCases.selectFontFamily(
                    element = _elementsState.value.selectedElement,
                    fontFamily = action.fontFamily,
                    color = _uiState.value.selectedColor
                )
                updateUi { copy(selectedFontFamily = action.fontFamily) }
            }
        }
    }

    private fun handleUiAction(action: UiAction) {
        when(action) {
            UiAction.HideColorPicker -> updateUi { copy(showColorPicker = false) }
            UiAction.HideTextInput -> updateUi { copy(showTextInput = false) }
            UiAction.HideToolsSelector -> updateUi { copy(showToolsSelector = false) }
            UiAction.ShowColorPicker -> updateUi { copy(showColorPicker = true) }
            UiAction.ShowTextInput -> updateUi { copy(showTextInput = true) }
            UiAction.ShowToolsSelector -> updateUi { copy(showToolsSelector = true) }
            is UiAction.SelectAspectRatio -> updateUi { copy(aspectRatio = action.aspectRatio) }
            is UiAction.SelectColor -> updateUi { copy(selectedColor = action.color) }
            is UiAction.SetPanelMode -> updateUi { copy(selectedPanelMode = action.mode) }
            is UiAction.SetCanvasMode -> updateUi {
                when(action.mode) {
                    CanvasMode.DEFAULT -> copy(selectedCanvasMode = action.mode)
                    CanvasMode.CROP,
                    CanvasMode.PENCIL,
                    CanvasMode.CREATE_STICKER,
                    CanvasMode.RUBBER -> {
                        when (_elementsState.value.selectedElement) {
                            null -> copy(selectedCanvasMode = CanvasMode.DEFAULT)
                            !is Img -> copy(selectedCanvasMode = CanvasMode.DEFAULT)
                            else -> copy(selectedCanvasMode = action.mode)
                        }

                    }
                }
            }
            is UiAction.Save -> TODO()
            is UiAction.SelectTool -> selectTool(action.toolType)
        }
    }

    private fun selectTool(toolType: ToolType) {

        hideSelectors()

        when(toolType) {
            ToolType.PickImageFromGallery -> {
                sendEvent(CanvasEvent.PickImage)
            }
            ToolType.AspectRatio -> updateUi { copy(selectedPanelMode = PanelMode.ASPECT_RATIO) }
            ToolType.CreateSticker -> updateUi { copy(selectedCanvasMode = CanvasMode.CREATE_STICKER) }
            ToolType.CropImage -> updateUi { copy(selectedCanvasMode = CanvasMode.CROP) }
            ToolType.Filters -> updateUi { copy(selectedPanelMode = PanelMode.FILTERS) }
            ToolType.Pencil -> updateUi { copy(selectedPanelMode = PanelMode.PENCIL) }
            ToolType.RemoveBg -> onAction(EditingAction.RemoveBackground)
            ToolType.Save -> TODO()
            ToolType.Stickers -> sendEvent(CanvasEvent.NavigateToStickersScreen)
            ToolType.Text -> updateUi { copy(showTextInput = true) }
        }
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

    private fun selectElement(index: Int) {

        if(index >= _elementsState.value.elements.size) return



        updateState { copy(
            selectedElementIndex = index
        ) }

        when(_elementsState.value.selectedElement) {
            is Img -> updateUi { copy(selectedPanelMode = PanelMode.IMAGE) }
            is TextModel -> updateUi { copy(selectedPanelMode = PanelMode.TEXT) }
        }
        hideSelectors()

    }


    private fun addImage(bitmap: Bitmap) {
        val newImg =  Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )

        elementsUseCases.addImage(
            list = _elementsState.value.elements,
            img = newImg
        ).also { result ->
            when(result) {
                is Result.Failure -> showError("Something went wrong...")
                is Result.Success<List<Element>> -> {
                    updateState { copy(
                        elements = result.data,
                        selectedElementIndex = result.data.size - 1,
                    ) }
                    updateUi { copy(
                        selectedCanvasMode = CanvasMode.DEFAULT,
                        selectedPanelMode = PanelMode.IMAGE
                    ) }

                }
            }
        }

        hideSelectors()

    }


    private fun showError(message: String) {
        updateUi { copy(showToolsSelector = false,) }
        sendEvent(CanvasEvent.ShowSnackbar(message))

    }

    private fun hideSelectors(
        textInput: Boolean = false,
        colorPicker: Boolean = false,
        toolsSelector: Boolean = false
    ) {
        updateUi { copy(
            showTextInput = textInput,
            showColorPicker = colorPicker,
            showToolsSelector = toolsSelector
        ) }
    }


    private inline fun updateState(block: ElementsState.() -> ElementsState) = _elementsState.update{ it.block() }

    private inline fun updateUi(block: UiState.() -> UiState) = _uiState.update { it.block() }

    private fun sendEvent(event: CanvasEvent) = viewModelScope.launch { _events.send(event) }
}