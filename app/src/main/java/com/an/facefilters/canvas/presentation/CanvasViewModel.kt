package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.domain.LimitedStack
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.model.CanvasMode
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PanelMode
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.domain.model.handle
import com.an.facefilters.canvas.domain.use_cases.editing.EditingUseCases
import com.an.facefilters.canvas.domain.use_cases.elements.ElementsUseCases
import com.an.facefilters.canvas.domain.use_cases.stickers.StickersUseCases
import com.an.facefilters.canvas.presentation.util.drawPaths
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

    private val _drawingState = MutableStateFlow(DrawingState())
    val drawingState= _drawingState.asStateFlow()

    private val _events = Channel<CanvasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val stateHistory = LimitedStack<List<Element>>()

    private var lastEvent: CanvasEvent? = null

    init {
        viewModelScope.launch {
            stickerUseCases.loadStickerState().also { result ->
                when(result) {
                    is Result.Failure -> showError(result.message)
                    is Result.Success<StickersState> -> _stickersState.value = result.data
                }
            }
        }
    }

    fun onAction(action: CanvasAction) {
        when(action) {
            is EditingAction -> handleEditingAction(action)
            is StickerAction -> handleStickerAction(action)
            is ElementAction -> handleElementAction(action)
            is UiAction -> handleUiAction(action)
            is DrawingAction -> handleDrawingAction(action)
            is TextAction -> handleTextAction(action)
        }
    }

    private fun handleTextAction(action: TextAction) {
        when(action) {
            is TextAction.AddText -> elementsUseCases.addText(
                text = action.text,
                elements = _elementsState.value.elements,
                fontFamily = _uiState.value.selectedFontFamily,
                color = _uiState.value.selectedColor
            ).also { newList ->
                saveHistory()
                _elementsState.update { it.copy(
                    elements = newList,
                    selectedElementIndex = newList.size - 1
                ) }
                hideSelectors()
                updateUi { copy(
                    selectedCanvasMode = CanvasMode.DEFAULT,
                    selectedPanelMode = PanelMode.TEXT
                ) }

            }
            is TextAction.ApplyTextStyle -> {
                updateUi { copy(
                    selectedFontFamily = action.fontFamily,
                    selectedColor = action.color
                ) }
                if(action.changeCurrentElement) {
                    elementsUseCases.applyTextStyle(
                        element = _elementsState.value.selectedElement,
                        fontFamily = action.fontFamily,
                        color = action.color
                    ).handle(
                        onSuccess = { updateElement(it) },
                        onFailure = ::showError
                    )
                }
            }
        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action){
            is DrawingAction.AddNewPath -> {
                _drawingState.update { it.copy(
                    paths = _drawingState.value.paths + _drawingState.value.currentPath,
                    currentPath = PathData(
                        color = _uiState.value.selectedColor,
                        path = emptyList(),
                        thickness = _drawingState.value.pathThickness
                    )
                ) }
            }
            is DrawingAction.AddRubberPath -> {
                _drawingState.update { it.copy(
                    paths = _drawingState.value.paths + _drawingState.value.currentPath,
                    currentPath = PathData(
                        color = Color.White,
                        path = emptyList(),
                        thickness = _drawingState.value.rubberSize
                    )
                ) }
            }
            is DrawingAction.SaveDrawings -> {

                val editedImg = _drawingState.value.editedImg ?: return

                val newBitmap = editedImg
                    .bitmap
                    .drawPaths(_drawingState.value.paths)
                val newOriginalBitmap = editedImg
                    .originalBitmap
                    .drawPaths(_drawingState.value.paths)
                updateElement(editedImg.copy(
                    bitmap = newBitmap,
                    originalBitmap = newOriginalBitmap
                ))
                _drawingState.update { it.copy(
                    paths = emptyList(),
                    editedImg = null,
                    currentPath = it.currentPath.reset()
                ) }

                sendEvent(CanvasEvent.PopBackStack)
            }
            is DrawingAction.SelectThickness -> {
                _drawingState.update { it.copy(
                    pathThickness = action.thickness
                ) }
            }
            DrawingAction.Cancel -> {
                _drawingState.update { it.copy(
                    paths = emptyList(),
                    editedImg = null,
                    currentPath = it.currentPath.reset()
                ) }

                sendEvent(CanvasEvent.PopBackStack)
            }

            is DrawingAction.UpdateCurrentPath -> {
                val currentPath = drawingState.value.currentPath
                _drawingState.update { it.copy(
                    currentPath = currentPath.copy(
                        path = currentPath.path + action.offset,
                        color = _uiState.value.selectedColor,
                        thickness = _drawingState.value.pathThickness
                    )
                ) }
            }

            DrawingAction.UndoPath -> {

                if(_drawingState.value.paths.isEmpty()) return

                _drawingState.update { it.copy(
                    paths = it
                        .paths
                        .toMutableList()
                        .apply { removeAt(this.lastIndex) }
                        .toList()
                ) }
            }

            DrawingAction.SaveRubber -> TODO()
        }
    }

    private fun handleStickerAction(action: StickerAction) {
        when(action) {
            is StickerAction.LoadSticker -> {
                viewModelScope.launch {
                    stickerUseCases.loadSticker(action.path).handle(
                        onSuccess = { addImage(it) },
                        onFailure = ::showError
                    )
                    _events.send(CanvasEvent.StickerAdded)
                }
                hideSelectors()


            }
            is StickerAction.CreateSticker -> {
                addImage(action.bitmap)
                /*try {
                    stickerUseCases.createNewSticker(
                        action.bitmap,
                        onStickerCreated = { sticker ->
                            addImage(sticker)
                        }
                    )
                } catch (e: Exception) {
                    showError(e.message.toString())
                }*/
                hideSelectors()
                updateUi { copy(selectedCanvasMode = CanvasMode.DEFAULT) }

            }
            is StickerAction.LoadStickersByCategory -> {
                _stickersState.update { it.copy(
                    selectedCategory = action.category
                ) }
                viewModelScope.launch {
                    stickerUseCases.loadStickerByCategory(action.category).handle(
                        onSuccess = { list ->
                            _stickersState.update { it.copy(
                                stickers = list,
                            ) }
                        },
                        onFailure = ::showError
                    )
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
                ).handle(
                    onSuccess = { updateElement(it) },
                    onFailure = ::showError
                )
            }
            is EditingAction.ChangeElementAlpha -> {
                editingUseCases.changeElementAlpha(
                    element = _elementsState.value.selectedElement,
                    alpha = action.alpha
                ).handle(
                    onSuccess = { updateElement(it) },
                    onFailure = ::showError
                )
            }
            is EditingAction.CropImage -> {
                editingUseCases.cropImage(
                    element = _elementsState.value.selectedElement,
                    srcRect = action.srcRect,
                    viewSize = action.viewSize
                ).handle(
                    onSuccess = { updateElement(it) },
                    onFailure = ::showError
                )
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
                ).handle(
                    onSuccess = {
                        updateElement(
                            newElement = it,
                            saveHistory = false
                        )
                    },
                    onFailure = ::showError
                )
            }

            EditingAction.TransformStart -> {
                saveHistory()
                updateUi { copy(showElementDetail = true) }
            }
            EditingAction.TransformEnd -> updateUi { copy(showElementDetail = false) }
        }
    }

    private fun handleElementAction(action: ElementAction) {
        when(action) {
            is ElementAction.AddImage -> addImage(action.bitmap)
            ElementAction.DeleteElement -> {
                elementsUseCases.deleteElement(
                    list = _elementsState.value.elements,
                    element = _elementsState.value.selectedElement
                ).handle(
                    onSuccess = { data ->
                        _elementsState.update { it.copy(
                            elements = data,
                            selectedElementIndex = null,
                        ) }
                        updateUi { copy(
                            selectedPanelMode = PanelMode.ELEMENTS
                        ) }

                    },
                    onFailure = ::showError
                )
            }
            is ElementAction.SelectElement -> selectElement(action.index)
            is ElementAction.UpdateElement -> updateElement(action.element)
            is ElementAction.UpdateElementOrder -> elementsUseCases.updateElementsOrder(
                from = action.fromIndex,
                to = action.toIndex,
                list = _elementsState.value.elements,
            ).handle(
                onSuccess = { data ->
                    _elementsState.update { it.copy(elements = data) }
                },
                onFailure = ::showError
            )

            ElementAction.Undo -> undo()
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
            is UiAction.SelectColor -> {
                updateUi {
                    copy(
                        selectedColor = action.color,
                        showColorPicker = false
                    )
                }
            }
            is UiAction.SetPanelMode -> updateUi { copy(selectedPanelMode = action.mode) }
            is UiAction.SetCanvasMode -> selectCanvasMode(action.mode)
            is UiAction.Save -> saveFile(
                width = action.width,
                height = action.height
            )
            is UiAction.SelectTool -> selectTool(action.toolType)
            UiAction.HideElementDetails -> updateUi { copy(showElementDetail = false) }
            UiAction.ShowElementDetails -> updateUi { copy(showElementDetail = true) }
        }
    }

    private fun selectCanvasMode(mode: CanvasMode) {
        updateUi {
            when(mode) {
                CanvasMode.DEFAULT -> {
                    val panelMode = when(_elementsState.value.selectedElement) {
                        is Img -> PanelMode.IMAGE
                        is TextModel -> PanelMode.TEXT
                        else -> PanelMode.ELEMENTS

                    }
                    copy(
                        selectedCanvasMode = mode,
                        selectedPanelMode = panelMode
                    )
                }
                CanvasMode.CROP,
                CanvasMode.CREATE_STICKER,
                CanvasMode.RUBBER,
                    -> {
                    when (_elementsState.value.selectedElement) {
                        null -> copy(selectedCanvasMode = CanvasMode.DEFAULT)
                        !is Img -> copy(selectedCanvasMode = CanvasMode.DEFAULT)
                        else -> copy(selectedCanvasMode = mode)
                    }

                }
            }
        }
    }

    private fun selectTool(toolType: ToolType) {

        hideSelectors()

        when(toolType) {
            ToolType.PickImageFromGallery -> { sendEvent(CanvasEvent.PickImage) }
            ToolType.AspectRatio -> updateUi { copy(selectedPanelMode = PanelMode.ASPECT_RATIO) }
            ToolType.CreateSticker -> updateUi { copy(selectedCanvasMode = CanvasMode.CREATE_STICKER) }
            ToolType.CropImage -> updateUi { copy(selectedCanvasMode = CanvasMode.CROP) }
            ToolType.Filters -> updateUi { copy(selectedPanelMode = PanelMode.FILTERS) }
            ToolType.RemoveBg -> onAction(EditingAction.RemoveBackground)
            ToolType.Stickers -> sendEvent(CanvasEvent.NavigateToStickersScreen)
            ToolType.Text -> updateUi { copy(showTextInput = true) }
            ToolType.Pencil -> {
                if(_elementsState.value.selectedElement !is Img) return

                _drawingState.update { it.copy(
                    editedImg = _elementsState.value.selectedElement as Img
                ) }
                sendEvent(CanvasEvent.NavigateToDrawingScreen)
            }

            ToolType.Delete -> handleElementAction(ElementAction.DeleteElement)
            ToolType.Rubber -> {
                if(_elementsState.value.selectedElement !is Img) return

                _drawingState.update { it.copy(
                    editedImg = _elementsState.value.selectedElement as Img
                ) }
                sendEvent(CanvasEvent.NavigateToRubberScreen)
            }
            is ToolType.Save -> {}
        }
    }

    private fun saveFile(width: Int, height: Int) {
        viewModelScope.launch {
            elementsUseCases.saveToGallery(
                elements = _elementsState.value.elements,
                width = width,
                height = height
            ).handle(
                onFailure = ::showError,
                onSuccess = ::showError
            )
        }

    }

    private fun updateElement(
        newElement: Element,
        saveHistory: Boolean = true
    ) {

        Log.d("TAG", "cvmc updateElement: ${_elementsState.value.selectedElement}")
        val index = _elementsState.value.selectedElementIndex ?: return

        if(saveHistory) saveHistory()

        updateElementState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    set(
                        index = index,
                        element = newElement
                    )
                }
                .toList()
        ) }

        Log.d("TAG", "cvmc after updateElement : ${_elementsState.value.selectedElement}")
    }

    private fun selectElement(index: Int) {

        if(index >= _elementsState.value.elements.size) return

        updateElementState { copy(
            selectedElementIndex = index
        ) }

        when(_elementsState.value.selectedElement) {
            is Img -> updateUi { copy(selectedPanelMode = PanelMode.IMAGE) }
            is TextModel -> updateUi { copy(selectedPanelMode = PanelMode.TEXT) }
        }
        hideSelectors()

    }


    private fun addImage(bitmap: Bitmap) {
        saveHistory()
        val newImg =  Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )

        elementsUseCases.addImage(
            list = _elementsState.value.elements,
            img = newImg
        ).handle(
            onSuccess = { data ->
                updateElementState { copy(
                    elements = data,
                    selectedElementIndex = data.size - 1,
                ) }
                updateUi { copy(
                    selectedCanvasMode = CanvasMode.DEFAULT,
                    selectedPanelMode = PanelMode.IMAGE
                ) }
            },
            onFailure = ::showError
        )

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

    private fun saveHistory() = stateHistory.push(_elementsState.value.elements)

    private fun undo() {
        if(!stateHistory.isEmpty()) {
            updateElementState { copy(elements = stateHistory.pop()!!) }
        }
    }


    private inline fun updateElementState(block: ElementsState.() -> ElementsState) = _elementsState.update{ it.block() }

    private inline fun updateUi(block: UiState.() -> UiState) = _uiState.update { it.block() }


    private fun sendEvent(event: CanvasEvent) = viewModelScope.launch {
        if (event != lastEvent) {
            _events.send(event)
            lastEvent = event
        }
    }}