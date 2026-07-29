package com.an.core_project.data

import android.util.Log
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.Point
import com.an.core_project.domain.ProjectEditor
import com.an.core_project.domain.ProjectRepository

class ProjectEditorImpl(
    private val projectRepository: ProjectRepository,
): ProjectEditor {

    val project = projectRepository.session.value

    override suspend fun addElement(element: DomainElement) {

        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy(elements = project.elements + element)
            )
        }

        Log.d("Add Image", "addElement: ${projectRepository.session.value}")


    }

    override suspend fun updateElement(
        index: Int,
        newElement: DomainElement,
        saveUndo: Boolean,
    ) {

        project?.let {
            projectRepository.updateProject(
                saveUndo = saveUndo,
                updatedProject = project.copy(
                    elements = project
                        .elements
                        .toMutableList()
                        .apply {
                            set(index, newElement)
                        }
                        .toList()
                )
            )
        }
    }

    override suspend fun transformSelectedElement(
        scale: Float,
        rotationDelta: Float,
        translation: Point,
        saveUndo: Boolean
    ) {
        val session = projectRepository.session.value ?: return
        val selectedElement = getSelectedElement() ?: return
        val index = session.selectedElementIndex ?: return

        val transformedElement = selectedElement.transform(
            scaleDelta = scale,
            rotationDelta = rotationDelta,
            translation = translation
        )

        updateElement(
            index = index,
            newElement = transformedElement,
            saveUndo = saveUndo
        )

    }

    override suspend fun removeElement(index: Int) {

        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy(
                    elements = project
                        .elements
                        .toMutableList()
                        .apply {
                            removeAt(index)
                        }
                        .toList(),
                    selectedElementIndex = null
                )
            )
        }

    }

    override suspend fun reorderElements(fromIndex: Int, toIndex: Int) {

        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy(
                    elements = project
                        .elements
                        .toMutableList()
                        .apply {
                            add(toIndex, removeAt(fromIndex))
                        }
                        .toList(),
                    selectedElementIndex = toIndex
                )
            )
        }

    }

    override suspend fun selectElement(index: Int) {

        project?.let {

            if(index < 0 || index >= project.elements.size) return


            projectRepository.updateProject(
                saveUndo = false,
                updatedProject = project.copy(
                    selectedElementIndex = index
                )
            )
        }
    }

    override suspend fun saveUndo() {
        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy()
            )
        }
    }

    override suspend fun undo() {

        project?.let {

            val undos = project.undos

            val previousState = undos
                .last()

            val newUndos = undos.subList(0, undos.size - 2)

            projectRepository.updateProject(
                saveUndo = false,
                updatedProject = project.copy(
                    undos = newUndos,
                    elements = previousState
                )
            )
        }

    }

    override fun getSelectedElement(): DomainElement? {
        val session = projectRepository.session.value ?: return null

        if(session.elements.isEmpty()) return null

        if(session.selectedElementIndex == null) return null

        return session.elements[session.selectedElementIndex]
    }


}