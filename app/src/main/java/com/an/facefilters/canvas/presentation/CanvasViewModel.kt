package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.DrawingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.PngFileManager
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
import com.an.facefilters.canvas.presentation.util.cropToRect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack

class CanvasViewModel(
    private val useCases: CanvasUseCaseProvider,
    private val stickerManager: StickerManager,
    private val subjectDetector: SubjectDetector,
    private val fileManager: PngFileManager
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

    val CanvasState.selectedElement: Element?
        get() = selectedElementIndex?.let { elements.getOrNull(it) }

    inline fun<reified T: Element> CanvasState.selectedAs(): T? = (selectedElement as? T)


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
                    sendEvent(CanvasEvent.StickerAdded)

                }
            }
        }
    }

    private fun handleToolAction(action: ToolAction) {
        when(action) {
            is ToolAction.SelectColor -> updateState { copy(
                selectedColor = action.color,
                showColorPicker = false
            ) }
            is ToolAction.SetMode -> updateState { copy(selectedMode = action.mode) }
            is ToolAction.AddText -> addText(action.text)
            is ToolAction.SelectTool -> selectTool(action.tool)
            ToolAction.Undo -> undo()
            ToolAction.Redo -> redo()
            is ToolAction.SelectFontFamily -> { selectFontFamily(action.fontFamily) }
            is ToolAction.SelectAspectRatio -> updateState { copy(aspectRatio = action.aspectRatio) }
            is ToolAction.Save ->  {
                fileManager.saveAsPng(
                    elements = _screenState.value.elements,
                    textMeasurer = action.textMeasurer
                ).also {
                    if(it == null) {
                        Log.d("TAG", "handleToolAction: null")
                    } else {
                        Log.d("TAG", "handleToolAction: save")
                    }
                }
            }
        }
    }

    private fun handleUiAction(action: UiAction) {
        when(action) {
            UiAction.ConsumeEvent -> sendEvent(CanvasEvent.None)
            UiAction.HideColorPicker -> updateState { copy(showColorPicker = false) }
            UiAction.HideTextInput -> updateState { copy(showTextInput = false) }
            UiAction.HideToolsSelector -> updateState { copy(showToolsSelector = false) }
            UiAction.ShowColorPicker -> updateState { copy(showColorPicker = true) }
            UiAction.ShowTextInput -> updateState { copy(showTextInput = true) }
            UiAction.ShowToolsSelector -> updateState { copy(showToolsSelector = true) }
        }
    }

    private fun handleDrawingAction(action: DrawingAction) {
        when(action) {
            is DrawingAction.DrawPath -> drawPath(action.offset)
            DrawingAction.EndDrawingPath -> addPath()
            DrawingAction.StartDrawingPath -> saveUndo()
            is DrawingAction.SelectThickness -> {
                updateState { copy(pathThickness = action.thickness) }
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
            is ElementAction.CropImage -> cropImage(action.srcRect, action.viewSize)
            ElementAction.DeleteElement -> deleteElement()
            is ElementAction.SelectElement -> {
                val mode = when(_screenState.value.elements[action.index]) {
                    is TextModel -> Mode.TEXT
                    is Img -> Mode.IMAGE
                    else -> Mode.ELEMENTS
                }
                updateState { copy(
                    selectedElementIndex = action.index,
                    selectedMode = mode
                ) }
            }
            is ElementAction.ApplyFilter -> {

                val oldImg = _screenState.value.selectedAs<Img>() ?: return

                oldImg.copy(
                    bitmap = action.filter.apply(oldImg.originalBitmap),
                    currentFilter = action.filter.name
                ).also { updateElement(it) }

            }
        }
    }

    private fun createSticker(bitmap: Bitmap) {
        subjectDetector.detectSubject(
            bitmap = bitmap,
            onSubjectDetected = { sticker ->
                sendEvent(CanvasEvent.StickerCreated)
                stickerManager.saveNewSticker(sticker)
                addImage(sticker)
            },
            onError = { showError("Something Went Wrong...") }
        )
    }

    private fun detectSubject() {
        screenState.value.selectedElement?.let { currentBitmap ->
            if(currentBitmap !is Img) {
                showError("Pick Image")
                return
            }
            var newBitmap: Bitmap? = null
            var newOriginalBitmap: Bitmap? = null
            subjectDetector.detectSubject(
                bitmap = currentBitmap.originalBitmap,
                onSubjectDetected = { newBitmap = it },
                onError = { showError(it) }
            )
            subjectDetector.detectSubject(
                bitmap = currentBitmap.originalBitmap,
                onSubjectDetected = { newOriginalBitmap = it },
                onError = { showError(it) }
            )
            if(newBitmap == null|| newOriginalBitmap == null) return

            currentBitmap.copy(
                bitmap = newBitmap!!,
                originalBitmap = newOriginalBitmap!!
            ).also { updateElement(it) }

        } ?: showError("Pick Image")

    }

    private fun selectFontFamily(fontFamily: FontFamily) {
        updateState { copy(
            selectedFontFamily = fontFamily
        ) }
        _screenState.value.selectedElement?.let { currentElement ->
            if(currentElement is TextModel) {
                useCases.selectFontFamily(
                    element = currentElement,
                    fontFamily = fontFamily,
                    color = _screenState.value.selectedColor
                ).also { updateElement(it) }
            }
        }
    }


    private fun deleteElement() {
        saveUndo()
        updateState { copy(
            elements = useCases.deleteElement(
                list = _screenState.value.elements,
                index = _screenState.value.selectedElementIndex
            ),
            selectedElementIndex = null,
        )}
    }

    private fun changeLayersAlpha(alpha: Float) {
        saveUndo()
        _screenState.value.selectedElement?.let { currentElement ->
            currentElement.setAlpha(alpha).also { updateElement(it) }

        }
    }

    private fun addText(text: String) {
        saveUndo()

        val currentElements = _screenState.value.elements

        updateState { copy(
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

    private fun cropImage(srcRect: Rect, viewSize: IntSize) {

        val croppedImg = _screenState.value.selectedElement as Img

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

        sendEvent(CanvasEvent.ImageCropped)
    }

    private fun showError(message: String) {
        updateState { copy(
            showToolsSelector = false,
        ) }
        sendEvent(CanvasEvent.ShowToast(message))

    }

    private fun undo() {
        if(undos.isNotEmpty()) {
            val previousState = undos.pop()
            redos.push(Undo(
                elements = _screenState.value.elements,
                paths = _screenState.value.paths
            ))
            updateState { copy(
                elements = previousState.elements,
                paths = previousState.paths
            ) }
        }
    }
    private fun redo() {
        if(redos.isNotEmpty()) {
            val nextState = redos.pop()
            saveUndo()
            updateState { copy(
                elements = nextState.elements,
                paths = nextState.paths
            ) }
        }
    }
    private fun addImage(bitmap: Bitmap) {
        saveUndo()
        val currentElements = _screenState.value.elements
        updateState { copy(
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
        updateState { copy(
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
        if(_screenState.value.selectedElement == null) return

        redos.clear()
        screenState.value
            .selectedElement!!
            .transform(
                scale = action.scale,
                rotation = action.rotation,
                offset = action.offset
            )
            .also { updateElement(it) }

    }
    private fun dragAndDrop(from: Int, to: Int) {
        updateState { copy(
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

        updateState { copy(
            elements = newList,
            showToolsSelector = false
        ) }
    }
    private fun drawPath(offset: Offset) {
        redos.clear()
        val currentPath = _screenState.value.drawnPath?.path
        if(currentPath == null) {
            updateState { copy(
                drawnPath = PathData(
                    color = _screenState.value.selectedColor,
                    path = emptyList<Offset>() + offset,
                    thickness = _screenState.value.pathThickness
                ),
                showToolsSelector = false
            ) }
        } else {
            updateState { copy(
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
                sendEvent(CanvasEvent.PickImage)
                updateState { copy(
                    showToolsSelector = false
                ) }
            }
            ToolType.Pencil -> {
                updateState { copy(
                    selectedMode = Mode.PENCIL,
                    showToolsSelector = false
                ) }
            }
            ToolType.Text -> {
                updateState { copy(
                    selectedMode = Mode.TEXT,
                    showToolsSelector = false,
                    showTextInput = true
                ) }
            }
            ToolType.RemoveBg -> {
                detectSubject()
            }
            ToolType.CropImage -> {
                _screenState.value.selectedElement?.let { currentElement ->
                    if(currentElement is Img) {
                        sendEvent(CanvasEvent.NavigateToCropScreen)
                    } else {
                        sendEvent(CanvasEvent.ShowToast("Pick image"))
                    }
                }?: sendEvent(CanvasEvent.ShowToast("Pick image"))
            }

            ToolType.CreateSticker -> {
                _screenState.value.selectedElement?.let {
                    if(_screenState.value.selectedElement is Img) {
                        sendEvent(CanvasEvent.NavigateToCreateStickerScreen)
                    } else {
                        sendEvent(CanvasEvent.ShowToast("Pick Image"))
                    }
                }?: sendEvent(CanvasEvent.ShowToast("Pick Image"))
            }

            ToolType.AspectRatio -> {
                updateState { copy(
                    selectedMode = Mode.ASPECT_RATIO,
                    showToolsSelector = false,
                ) }
            }

            ToolType.Stickers -> {
                sendEvent(CanvasEvent.NavigateToStickersScreen)
            }

            ToolType.Filters -> {
                if(_screenState.value.selectedElement !is Img) return

                updateState { copy(
                    selectedMode = Mode.FILTERS,
                    showToolsSelector = false,
                ) }
            }

            ToolType.Save -> {

            }
        }
    }

    private inline fun updateState(block: CanvasState.() -> CanvasState) = _screenState.update{ it.block() }

    private fun sendEvent(event: CanvasEvent) = viewModelScope.launch { _events.send(event) }
}