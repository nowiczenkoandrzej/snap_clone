package com.an.feature_text.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.EditorState
import com.an.core_editor.presentation.EditorUiState
import com.an.core_editor.presentation.toDomainColor
import com.an.core_editor.presentation.toDomainFontFamily
import com.an.core_editor.presentation.toEditorUiState
import com.an.feature_text.domain.TextUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TextViewModel(
    private val editorRepository: EditorRepository,
    private val useCases: TextUseCases
): ViewModel() {

    val editorState = editorRepository
        .state

    private val _textState = MutableStateFlow(TextScreenState())
    val textState = _textState.asStateFlow()

    private val _events = Channel<TextEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    fun onAction(action: TextAction) {
        viewModelScope.launch {
            when(action) {
                is TextAction.AddText -> useCases.addText(
                    text = _textState.value.currentText,
                    fontSize = 60f,
                    fontColor = _textState.value.textColor.toDomainColor(),
                    fontFamily = _textState.value.fontItem.toDomainFontFamily()
                )
                is TextAction.ApplyFontFamily -> _textState.update { it.copy(
                    fontItem = action.fontItem
                ) }
                is TextAction.ApplyTextColor -> _textState.update { it.copy(
                    textColor = action.color
                ) }
                is TextAction.ChangeFontColor -> useCases.updateTextColor(
                    color = action.color.toDomainColor()
                )
                is TextAction.ChangeFontFamily -> useCases.updateFontFamily(
                    fontFamily = action.fontItem.toDomainFontFamily()
                )
                TextAction.Cancel -> TODO()
                TextAction.ToggleColorPicker -> _textState.update { it.copy(
                    showColorPicker = !_textState.value.showColorPicker
                ) }
                is TextAction.Type -> _textState.update { it.copy(
                    currentText = action.text
                ) }
            }
        }

    }
}