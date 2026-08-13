package com.an.core_project.domain

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Point
import kotlinx.coroutines.flow.Flow

interface ProjectEditor {

    val selectedImage: Flow<DomainImageModel?>

    suspend fun addElement(element: DomainElement)
    suspend fun updateElement(newElement: DomainElement, saveUndo: Boolean = true)
    suspend fun transformSelectedElement(scale: Float, rotationDelta: Float, translation: Point, saveUndo: Boolean = false)
    suspend fun removeElement(index: Int)
    suspend fun reorderElements(fromIndex: Int, toIndex: Int)
    suspend fun selectElement(index: Int)
    suspend fun saveUndo()
    suspend fun undo()
    fun getSelectedElement(): DomainElement?

}