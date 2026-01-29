package com.an.core_project.domain

import com.an.core_editor.domain.model.DomainElement
import kotlinx.coroutines.flow.StateFlow

interface ProjectSessionManager {

    val state: StateFlow<Project?>
    val selectedElement: StateFlow<DomainElement?>

    fun loadProject(id: Int)
    fun addElement(element: DomainElement)
    fun updateElement(
        index: Int,
        newElement: DomainElement,
        saveUndo: Boolean = true,
    )
    fun removeElement(index: Int)
    fun reorderElements(fromIndex: Int, toIndex: Int)
    fun selectElement(index: Int)
    fun undo()

}