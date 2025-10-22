package com.an.core_editor.data

import android.util.Log
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.model.DomainElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorRepositoryImpl: EditorRepository {

    private val undoStack = UndoStack<EditorState>()

    private val _state = MutableStateFlow(EditorState())
    override val state: StateFlow<EditorState> = _state.asStateFlow()

    override suspend fun addElement(element: DomainElement) {
        undoStack.push(_state.value)
        _state.update { it.copy(
            selectedElementIndex = it.elements.size,
            elements = it.elements + element
        ) }
    }

    override suspend fun updateElement(
        index: Int,
        newElement: DomainElement,
        saveUndo: Boolean
    ) {


        if(saveUndo) undoStack.push(_state.value)
        _state.update { it.copy(
            elements = _state
                .value
                .elements
                .toMutableList()
                .apply {
                    set(index, newElement)
                }
                .toList()
        ) }
        Log.d("TAG", "updateElement: ImageFilterScreen ${_state.value.elements[_state.value.selectedElementIndex!!]}")

    }

    override suspend fun removeElement(index: Int) {
        undoStack.push(_state.value)
        _state.update { it.copy(
            elements = it.elements
                .toMutableList()
                .apply {
                    removeAt(index)
                }
                .toList(),
            selectedElementIndex = null
        ) }
    }

    override fun getSelectedElement(): DomainElement? {
        if(state.value.elements.isEmpty()) return null

        if(state.value.selectedElementIndex == null) return null


        return state.value.elements[state.value.selectedElementIndex!!]
    }

    override suspend fun reorderElements(fromIndex: Int, toIndex: Int) {
        undoStack.push(_state.value)
        _state.update { it.copy(
            elements = it.elements
                .toMutableList()
                .apply {
                    add(toIndex, removeAt(fromIndex))
                }
                .toList(),
            selectedElementIndex = toIndex
        ) }
    }

    override suspend fun selectElement(index: Int) {
        _state.update { it.copy(
            selectedElementIndex = index
        ) }
    }

    override suspend fun undo() {
        if(!undoStack.isEmpty())
            _state.value = undoStack.pop()!!
    }

    override suspend fun clear() {
        undoStack.push(_state.value)
        _state.update { it.copy(
            elements = emptyList(),
            selectedElementIndex = null
        ) }
    }
}