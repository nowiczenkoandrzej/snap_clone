package com.an.facefilters.canvas.presentation.vm

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.ElementsState
import com.an.facefilters.canvas.domain.PngFileManager
import com.an.facefilters.canvas.domain.StickerAction
import com.an.facefilters.canvas.domain.ToolAction
import com.an.facefilters.canvas.domain.UiAction
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.TextModel
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.domain.use_cases.CanvasUseCaseProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CanvasViewModel(
    private val editingVM: EditingViewModel,
    private val stickerVM: StickerViewModel,
    private val actionChannel: ReceiveChannel<ElementAction>,


    private val useCases: CanvasUseCaseProvider,
    private val fileManager: PngFileManager,
): ViewModel() {
    private val _screenState = MutableStateFlow(CanvasState())
    val screenState = _screenState.asStateFlow()


    private val _elementsState = MutableStateFlow(ElementsState())
    val elementsState = _elementsState.asStateFlow()

    private val _events = Channel<CanvasEvent?>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            for (action in actionChannel) {
                handleElementAction(action)
            }
        }
    }

    fun onAction(action: CanvasAction) {

        when(action) {
            is EditingAction -> editingVM.onAction(action)
            is StickerAction -> stickerVM.onAction(action)
            is ToolAction -> handleToolAction(action)
            is UiAction -> handleUiAction(action)
            is ElementAction -> handleElementAction(action)
        }
    }


    private fun handleElementAction(action: ElementAction) {
        when(action) {
            is ElementAction.AddImage -> addImage(action.bitmap)
            ElementAction.DeleteElement -> deleteElement()
            is ElementAction.SelectElement -> selectElement(action.index)
            is ElementAction.UpdateElement -> updateElement(action.element)
            is ElementAction.UpdateElementOrder -> updateElementsOrder(action.fromIndex, action.toIndex)
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
            selectedElement = _elementsState
                .value
                .elements[index]
        ) }
    }

    private fun updateElementsOrder(from: Int, to: Int) {

        val size = _elementsState.value.elements.size

        if(from >= size || from < 0 || to >= size || to < 0) return

        updateState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    add(to, removeAt(from))
                }
                .toList()
        )}
    }

    private fun deleteElement() {
        val selectedElement = _elementsState.value.selectedElement ?: return

        updateState { copy(
            elements = _elementsState
                .value
                .elements
                .toMutableList()
                .apply {
                    remove(selectedElement)
                }
                .toList()
        ) }

    }

    private fun addImage(bitmap: Bitmap) {
        val newList = _elementsState.value.elements + Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )
        updateState { copy(
            elements = newList,
            selectedElement = newList.last()
        ) }
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
            UiAction.HideColorPicker -> updateState { copy(showColorPicker = false) }
            UiAction.HideTextInput -> updateState { copy(showTextInput = false) }
            UiAction.HideToolsSelector -> updateState { copy(showToolsSelector = false) }
            UiAction.ShowColorPicker -> updateState { copy(showColorPicker = true) }
            UiAction.ShowTextInput -> updateState { copy(showTextInput = true) }
            UiAction.ShowToolsSelector -> updateState { copy(showToolsSelector = true) }
        }
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


    private fun showError(message: String) {
        updateState { copy(
            showToolsSelector = false,
        ) }
        sendEvent(CanvasEvent.ShowToast(message))

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

    private inline fun updateState(block: ElementsState.() -> ElementsState) = _elementsState.update{ it.block() }

    private fun sendEvent(event: CanvasEvent) = viewModelScope.launch { _events.send(event) }
}