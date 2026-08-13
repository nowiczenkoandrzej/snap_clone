package com.an.core_project.data

import android.util.Log
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Point
import com.an.core_project.domain.ProjectEditor
import com.an.core_project.domain.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProjectEditorImpl(
    private val projectRepository: ProjectRepository,
): ProjectEditor {

    val session = projectRepository.session

    override val selectedImage: Flow<DomainImageModel?> =
        session.map { project ->
            val index = project?.selectedElementIndex ?: return@map null
            val element = project.elements.getOrNull(index)
            element as? DomainImageModel
        }.distinctUntilChanged()

    override suspend fun addElement(element: DomainElement) {

        val project = session.value

        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy(elements = project.elements + element)
            )
        }

        Log.d("Add Image", "addElement: ${projectRepository.session.value}")


    }

    override suspend fun updateElement(
        newElement: DomainElement,
        saveUndo: Boolean,
    ) {

        Log.d("TAG elements", "updateElement: $newElement")

        val project = session.value

        if(project != null && project.selectedElementIndex != null) {
            projectRepository.updateProject(
                saveUndo = saveUndo,
                updatedProject = project.copy(
                    elements = project
                        .elements
                        .toMutableList()
                        .apply {
                            set(project.selectedElementIndex, newElement)
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
        val selectedElement = getSelectedElement() ?: return

        val transformedElement = selectedElement.transform(
            scaleDelta = scale,
            rotationDelta = rotationDelta,
            translation = translation
        )

        updateElement(
            newElement = transformedElement,
            saveUndo = saveUndo
        )

    }

    override suspend fun removeElement(index: Int) {
        val project = session.value

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
        val project = session.value

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
        val project = session.value

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
        val project = session.value
        project?.let {
            projectRepository.updateProject(
                saveUndo = true,
                updatedProject = project.copy()
            )
        }
    }

    override suspend fun undo() {
        val project = session.value

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