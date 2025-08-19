package com.an.core_editor.domain

import com.an.core_editor.domain.model.DomainElement
import kotlinx.coroutines.flow.StateFlow

interface EditorRepository {
    val state: StateFlow<EditorState>

    suspend fun addElement(element: DomainElement)
    suspend fun updateElement(index: Int, newElement: DomainElement, saveUndo: Boolean = true)
    suspend fun removeElement(index: Int)
    suspend fun reorderElements(fromIndex: Int, toIndex: Int)
    suspend fun selectElement(index: Int)
    suspend fun undo()
    suspend fun clear()
    fun getSelectedElement(): DomainElement?

}