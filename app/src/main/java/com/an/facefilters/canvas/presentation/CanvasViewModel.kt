package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.DrawingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.StickerAction
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.domain.StickersState
import com.an.facefilters.canvas.domain.ToolAction
import com.an.facefilters.canvas.domain.UiAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.domain.model.Undo
import com.an.facefilters.canvas.domain.use_cases.CanvasUseCaseProvider
import com.an.facefilters.canvas.domain.use_cases.DetectionException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack

class CanvasViewModel(
    private val useCases: CanvasUseCaseProvider,
    private val stickerManager: StickerManager
): ViewModel() {
    private val _screenState = MutableStateFlow(CanvasState())
    val screenState = _screenState.asStateFlow()

    private val _stickersState = MutableStateFlow(StickersState(
        categories = stickerManager.getCategories(),
        stickers = stickerManager.loadStickers(StickerCategory.EMOJIS),
        userStickers = stickerManager.loadUserStickers()
    ))

    val stickersState = _stickersState.asStateFlow()

    private val _events = Channel<CanvasEvent?>()
    val events = _events.receiveAsFlow()

    private val undos = Stack<Undo>()
    private val redos = Stack<Undo>()

    fun onAction(action: CanvasAction) {

        when(action) {
            is ToolAction -> handleToolAction(action)
            is UiAction -> handleUiAction(action)
            is DrawingAction -> handleDrawingAction(action)
            is ElementAction -> handleElementAction(action)
            is StickerAction -> handleStickerAction(action)
        }
    }

    private fun handleStickerAction(action: StickerAction) {
        when(action) {
            is StickerAction.CreateSticker -> { createSticker(action.bitmap) }
            StickerAction.LoadCategories -> {

            }
            is StickerAction.LoadStickers -> {
                _stickersState.update { it.copy(
                    stickers = stickerManager.loadStickers(action.category),
                    selectedCategory = action.category
                ) }
            }

            is StickerAction.AddSticker -> {
                viewModelScope.launch {
                    val bitmap = stickerManager.loadPngAsBitmap(action.path)
                    addImage(bitmap)
                    _events.send(CanvasEvent.StickerAdded)
                }
            }
        }
    }

    private fun handleToolAction(action: ToolAction) {
        when(action) {
            is ToolAction.SelectColor -> _screenState.update { it.copy(
                    selectedColor = action.color,
                    showColorPicker = false
                ) }
            is ToolAction.SetMode -> _screenState.update { it.copy(selectedMode = action.mode) }
            is ToolAction.AddText -> addText(action.text)
            is ToolAction.SelectTool -> selectTool(action.tool)
            ToolAction.Undo -> undo()
            ToolAction.Redo -> redo()
            is ToolAction.SelectFontFamily -> { selectFontFamily(action.fontFamily) }
            is ToolAction.SelectAspectRatio -> _screenState.update { it.copy(aspectRatio = action.aspectRatio) }
        }
    }

    private fun handleUiAction(action: UiAction) {
        when(action) {
            UiAction.ConsumeEvent -> viewModelScope.launch { _events.send(null) }
            UiAction.HideColorPicker -> _screenState.update { it.copy(showColorPicker = false) }
            UiAction.HideTextInput -> _screenState.update { it.copy(showTextInput = false) }
            UiAction.HideToolsSelector -> _screenState.update { it.copy(showToolsSelector = false) }
            UiAction.ShowColorPicker -> _screenState.update { it.copy(showColorPicker = true) }
            UiAction.ShowTextInput -> _screenState.update { it.copy(showTextInput = true) }
            UiAction.ShowToolsSelector -> _screenState.update { it.copy(showToolsSelector = true) }
        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action) {
            is DrawingAction.DrawPath -> drawPath(action.offset)
            DrawingAction.EndDrawingPath -> addPath()
            DrawingAction.StartDrawingPath -> saveUndo()
            is DrawingAction.SelectThickness -> {
                _screenState.update { it.copy(
                    pathThickness = action.thickness
                ) }
            }
        }
    }

    private fun handleElementAction(action: ElementAction) {
        when(action) {
            is ElementAction.AddImage -> addImage(action.bitmap)
            is ElementAction.DragAndDropElement -> dragAndDrop(action.fromIndex, action.toIndex)
            is ElementAction.ChangeSliderPosition -> changeLayersAlpha(action.alpha)
            is ElementAction.TransformElement -> transformLayer(action)
            ElementAction.TransformStart -> { saveUndo() }
            is ElementAction.CropImage -> cropImage(action.bitmap)
            ElementAction.DeleteElement -> deleteElement()
            is ElementAction.SelectElement -> {
                val mode = when(_screenState.value.elements[action.index]) {
                    is TextModel -> Mode.TEXT
                    is Img -> Mode.IMAGE
                    else -> Mode.ELEMENTS
                }
                _screenState.update { it.copy(
                    selectedElementIndex = action.index,
                    selectedMode = mode
                ) }
            }
            is ElementAction.ApplyFilter -> {
                val index = _screenState.value.selectedElementIndex ?: return

                val olgImg = _screenState.value.elements[index] as Img

                val newImg = olgImg.copy(
                    bitmap = action.filter.apply(olgImg.originalBitmap),
                    currentFilter = action.filter.name
                )

                updateElement(newImg)
            }
        }
    }

    private fun createSticker(bitmap: Bitmap) {
        useCases.detectSubject(
            bitmap = bitmap,
            onDetect = { sticker ->
                if(sticker != null)
                    viewModelScope.launch {
                        stickerManager.saveNewSticker(sticker)
                        addImage(sticker)
                        _events.send(CanvasEvent.StickerCreated)
                    }
                else
                    showError("Something Went Wrong...")
            }
        )
    }

    private fun detectSubject() {
        screenState.value.selectedElementIndex?.let {
            val currentBitmap = screenState.value.elements[it]
            if(currentBitmap !is Img) {
                showError("Pick Image")
                return
            }
            try {
                useCases.detectSubject(
                    bitmap = currentBitmap.bitmap,
                    onDetect = { newBitmap ->
                        if(newBitmap != null)
                            updateElement(Img(
                                bitmap = newBitmap,
                                originalBitmap = currentBitmap.originalBitmap
                            ))
                        else showError("Something Went Wrong...")
                    }
                )
            } catch (e: DetectionException) {
                showError(e.message.toString())
            } catch (e: Exception) {
                showError(e.message.toString())
            }
        } ?: showError("Pick Image")

    }

    private fun selectFontFamily(fontFamily: FontFamily) {
        _screenState.update { it.copy(
            selectedFontFamily = fontFamily
        ) }
        _screenState.value.selectedElementIndex?.let {
            val currentElement = _screenState.value.elements[it]
            if(currentElement is TextModel) {
                val newText = useCases.selectFontFamily(
                    element = currentElement,
                    fontFamily = fontFamily,
                    color = _screenState.value.selectedColor
                )
                updateElement(newText)
            }
        }
    }


    private fun deleteElement() {
        saveUndo()
        _screenState.update { it.copy(
            elements = useCases.deleteElement(
                list = _screenState.value.elements,
                index = _screenState.value.selectedElementIndex
            ),
            selectedElementIndex = null,
        )}
    }

    private fun changeLayersAlpha(alpha: Float) {
        saveUndo()
        _screenState.value.selectedElementIndex?.let {
            val newElement = _screenState.value.elements[it].setAlpha(alpha)
            updateElement(newElement)
        }
    }

    private fun addText(text: String) {
        saveUndo()

        val currentElements = _screenState.value.elements

        _screenState.update { it.copy(
            elements = currentElements + TextModel(
                text = text,
                textStyle = TextStyle(
                    fontSize = 60.sp,
                    color = _screenState.value.selectedColor,
                    fontFamily = _screenState.value.selectedFontFamily
                ),
                p1 = Offset(0f, 0f)
            ),
            showTextInput = false,
            selectedMode = Mode.TEXT,
            selectedElementIndex = currentElements.size
        ) }
    }

    private fun cropImage(cropped: Bitmap) {
        viewModelScope.launch {
            try {
                val newImg = useCases.cropImage(
                    state = _screenState.value,
                    cropped = cropped
                )
                updateElement(newImg)
                _events.send(CanvasEvent.ImageCropped)
            } catch (e: Exception) {
                showError(e.message.toString())
            }
        }
    }

    private fun showError(message: String) {
        _screenState.update { it.copy(
            showToolsSelector = false,
        ) }
        viewModelScope.launch {
            _events.send(CanvasEvent.ShowToast(message))
        }
    }

    private fun undo() {
        if(undos.isNotEmpty()) {
            val previousState = undos.pop()
            redos.push(Undo(
                elements = _screenState.value.elements,
                paths = _screenState.value.paths
            ))
            _screenState.update { it.copy(
                elements = previousState.elements,
                paths = previousState.paths
            ) }
        }
    }
    private fun redo() {
        if(redos.isNotEmpty()) {
            val nextState = redos.pop()
            saveUndo()
            _screenState.update { it.copy(
                elements = nextState.elements,
                paths = nextState.paths
            ) }
        }
    }
    private fun addImage(bitmap: Bitmap) {
        saveUndo()
        val currentElements = _screenState.value.elements
        _screenState.update { it.copy(
            elements = currentElements + Img(
                bitmap = bitmap,
                originalBitmap = bitmap
            ),
            selectedElementIndex = currentElements.size,
            selectedMode = Mode.IMAGE,
            showToolsSelector = false
        ) }
    }
    private fun addPath() {
        val newPath = _screenState.value.drawnPath ?: return
        _screenState.update { it.copy(
            paths = _screenState.value.paths + newPath,
            drawnPath = null
        ) }
    }
    private fun saveUndo() {
        undos.push(Undo(
            elements = _screenState.value.elements,
            paths = _screenState.value.paths
        ))
    }
    private fun transformLayer(action: ElementAction.TransformElement) {
        if(screenState.value.selectedElementIndex == null) return
        if(screenState.value.elements.size <= screenState.value.selectedElementIndex!!) {
            _screenState.update { it.copy(
                selectedElementIndex = null
            ) }
            return
        }
        redos.clear()
        val updatedElement = screenState
            .value
            .elements[screenState.value.selectedElementIndex!!]
            .transform(
                scale = action.scale,
                rotation = action.rotation,
                offset = action.offset
            )
        updateElement(updatedElement)
    }
    private fun dragAndDrop(from: Int, to: Int) {
        _screenState.update { it.copy(
            elements = useCases
                .updateElementsOrder(
                    list = _screenState.value.elements,
                    from = from,
                    to = to
                ),
            selectedElementIndex = to
        )}
    }
    private fun updateElement(newElement: Element) {
        val newList = screenState
            .value
            .elements
            .toMutableList()
            .apply {
                this[screenState.value.selectedElementIndex!!] = newElement
            }
            .toList()

        _screenState.update { it.copy(
            elements = newList,
            showToolsSelector = false
        ) }
    }
    private fun drawPath(offset: Offset) {
        redos.clear()
        val currentPath = _screenState.value.drawnPath?.path
        if(currentPath == null) {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = emptyList<Offset>() + offset,
                    thickness = _screenState.value.pathThickness
                ),
                showToolsSelector = false
            ) }
        } else {
            _screenState.update { it.copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = currentPath + offset,
                    thickness = _screenState.value.pathThickness
                ),
                showToolsSelector = false
            ) }
        }
    }
    private fun selectTool(type: ToolType) {
        when(type) {
            ToolType.AddPhoto -> {
                viewModelScope.launch {
                    _events.send(CanvasEvent.PickImage)
                }
                _screenState.update { it.copy(
                    showToolsSelector = false
                ) }
            }
            ToolType.Pencil -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.PENCIL,
                    showToolsSelector = false
                ) }
            }
            ToolType.Text -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.TEXT,
                    showToolsSelector = false,
                    showTextInput = true
                ) }
            }
            ToolType.RemoveBg -> {
                detectSubject()
            }
            ToolType.CropImage -> {
                viewModelScope.launch {
                    _screenState.value.selectedElementIndex?.let {
                        if(_screenState.value.elements[_screenState.value.selectedElementIndex!!] is Img) {
                            _events.send(CanvasEvent.NavigateToCropScreen)
                        } else {
                            _events.send(CanvasEvent.ShowToast("Pick Image"))
                        }
                    }?: _events.send(CanvasEvent.ShowToast("Pick Image"))

                }
            }

            ToolType.CreateSticker -> {
                viewModelScope.launch {
                    _screenState.value.selectedElementIndex?.let {
                        if(_screenState.value.elements[_screenState.value.selectedElementIndex!!] is Img) {
                            _events.send(CanvasEvent.NavigateToCreateStickerScreen)
                        } else {
                            _events.send(CanvasEvent.ShowToast("Pick Image"))
                        }
                    }?: _events.send(CanvasEvent.ShowToast("Pick Image"))

                }
            }

            ToolType.AspectRatio -> {
                _screenState.update { it.copy(
                    selectedMode = Mode.ASPECT_RATIO,
                    showToolsSelector = false,
                ) }
            }

            ToolType.Stickers -> {
                viewModelScope.launch {
                    _events.send(CanvasEvent.NavigateToStickersScreen)
                }
            }
        }
    }
}