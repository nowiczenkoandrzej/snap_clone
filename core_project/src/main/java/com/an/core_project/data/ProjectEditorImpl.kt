package com.an.core_project.data

import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import com.an.core_project.domain.ProjectRepository

class ProjectEditorImpl(
    private val projectRepository: ProjectRepository,
    private val bitmapCache: BitmapCache
): ProjectEditor {


    override suspend fun addElement(element: DomainElement) {

        projectRepository.updateProject { project ->
            Result.Success(
                project.copy(elements = project.elements + element)
            )
        }



    }

    override suspend fun updateElement(
        index: Int,
        newElement: DomainElement,
        saveUndo: Boolean,
    ) {
        projectRepository.updateProject(saveUndo) { project ->
            Result.Success(project.copy(
                elements = project
                    .elements
                    .toMutableList()
                    .apply {
                        set(index, newElement)
                    }
                    .toList()
            ))
        }
    }

    override suspend fun removeElement(index: Int) {
        projectRepository.updateProject { project ->
            Result.Success(project.copy(
                elements = project
                    .elements
                    .toMutableList()
                    .apply {
                        removeAt(index)
                    }
                    .toList(),
                selectedElementIndex = null
            ))
        }
    }

    override suspend fun reorderElements(fromIndex: Int, toIndex: Int) {
        projectRepository.updateProject { project ->
            Result.Success(project.copy(
                elements = project
                    .elements
                    .toMutableList()
                    .apply {
                        add(toIndex, removeAt(fromIndex))
                    }
                    .toList(),
                selectedElementIndex = toIndex
            ))
        }
    }

    override suspend fun selectElement(index: Int) {
        projectRepository.updateProject { project ->
            Result.Success(project.copy(
                selectedElementIndex = index
            ))
        }
    }

    override suspend fun undo() {
        projectRepository.updateProject { project ->
            val previousState = project
                .undos.last()

            val newUndos = project.undos
                .toMutableList()
                .apply {
                    removeAt(project.undos.size - 1)
                }
                .toList()

            Result.Success(project.copy(
                elements = previousState,
                undos = newUndos
            ))
        }
    }

    override fun getSelectedElement(): DomainElement? {
        val session = projectRepository.session.value ?: return null

        if(session.elements.isEmpty()) return null

        if(session.selectedElementIndex == null) return null

        return session.elements[session.selectedElementIndex]
    }


}