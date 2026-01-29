package com.an.core_project.data

import com.an.core_editor.domain.model.DomainElement
import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProjectSessionManagerImpl(
    private val projectRepository: ProjectRepository
): ProjectSessionManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow<Project?>(null)
    override val state: StateFlow<Project?> = _state.asStateFlow()

    override val selectedElement: StateFlow<DomainElement?> =
        _state.map { project ->
            val index = project?.selectedElementIndex
            if (
                project == null ||
                index == null ||
                index !in project.elements.indices
            ) {
                null
            } else {
                project.elements[index]
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    override fun loadProject(id: Int) {

        _state.value = projectRepository.load(id)
    }

    override fun addElement(element: DomainElement) {
        saveUndo()
        updateProject { it.copy(
            selectedElementIndex = it.elements.size,
            elements = it.elements + element
        ) }

    }

    override fun updateElement(index: Int, newElement: DomainElement, saveUndo: Boolean) {

        if(saveUndo) saveUndo()

        updateProject { it.copy(
            elements = _state
                .value
            !!.elements
                .toMutableList()
                .apply {
                    set(index, newElement)
                }
                .toList()
        ) }

    }

    override fun removeElement(index: Int) {
        saveUndo()

        updateProject { it.copy(
            elements = it.elements
                .toMutableList()
                .apply {
                    removeAt(index)
                }
                .toList(),
            selectedElementIndex = null
        ) }
    }

    override fun reorderElements(fromIndex: Int, toIndex: Int) {
        saveUndo()

        updateProject { it.copy(
            elements = it.elements
                .toMutableList()
                .apply {
                    add(toIndex, removeAt(fromIndex))
                }
                .toList(),
            selectedElementIndex = toIndex
        ) }

    }

    override fun selectElement(index: Int) {
        updateProject { it.copy(
            selectedElementIndex = index
        ) }
    }

    override fun undo() {
        val project = _state.value ?: return
        val previous = project.undos.lastOrNull() ?: return

        updateProject { it.copy(
            elements = previous,
            undos = project.undos.dropLast(1),
            selectedElementIndex = null
        ) }
    }


    private fun saveUndo() {
        val project = _state.value ?: return

        updateProject { it.copy(
            undos = it.undos + listOf(project.elements)
        ) }
    }

    private inline fun updateProject(block: (Project) -> Project) {
        _state.update { project ->
            project?.let(block)
        }
    }

}