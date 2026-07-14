package com.an.core_project.domain

import com.an.core_editor.domain.model.DomainElement

interface ProjectEditor {

    suspend fun addElement(element: DomainElement)
    suspend fun updateElement(index: Int, newElement: DomainElement, saveUndo: Boolean = true)
    suspend fun removeElement(index: Int)
    suspend fun reorderElements(fromIndex: Int, toIndex: Int)
    suspend fun selectElement(index: Int)
    suspend fun undo()
    fun getSelectedElement(): DomainElement?

}