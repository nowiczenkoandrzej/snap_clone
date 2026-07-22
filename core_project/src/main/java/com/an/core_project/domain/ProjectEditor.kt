package com.an.core_project.domain

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.Point

interface ProjectEditor {

    suspend fun addElement(element: DomainElement)
    suspend fun updateElement(index: Int, newElement: DomainElement, saveUndo: Boolean = true)
    suspend fun transformSelectedElement(scale: Float, rotationDelta: Float, translation: Point, saveUndo: Boolean = false)
    suspend fun removeElement(index: Int)
    suspend fun reorderElements(fromIndex: Int, toIndex: Int)
    suspend fun selectElement(index: Int)
    suspend fun saveUndo()
    suspend fun undo()
    fun getSelectedElement(): DomainElement?

}