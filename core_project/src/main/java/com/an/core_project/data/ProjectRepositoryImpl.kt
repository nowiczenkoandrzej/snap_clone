package com.an.core_project.data

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.ProjectDataSource
import com.an.feature_image_rendering.ImageRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val mapper: EntityMapper,
    private val elementSerializer: ElementSerializer,
    private val imageRenderer: ImageRenderer
): ProjectRepository {

    private val _session = MutableStateFlow<Project?>(null)
    override val session: StateFlow<Project?> = _session.asStateFlow()


    override suspend fun loadProject(id: Long) {
        val projectEntity = projectDataSource
            .getProjectById(id)

        if(projectEntity != null)
            _session.value = mapper.mapProjectEntityToDomainModel(projectEntity)
    }

    override suspend fun saveProject() {

        val project = session.value ?: return

        val elementsSourcePath = elementSerializer.saveElements(project.elements)
        //val thumbnailBitmap = imageRenderer.renderImage(project.elements)

        projectDataSource.insertProject(
            id = project.id,
            elementsSourcePath = elementsSourcePath,
            aspectRatio = project.aspectRatio.toDouble(),
            undosSourcePath = elementsSourcePath,
            lastChange = System.currentTimeMillis(),
            thumbnailSourcePath = ""
        )
    }



    override suspend fun loadProjectSummaries(): List<ProjectSummary> {

        val summaries = projectDataSource.getProjectSummaries()


        TODO("not implemented yet")

    }

    override suspend fun startNewProject() {
        _session.value = Project()
    }

    override suspend fun updateProject(
        saveUndo: Boolean,
        transform: (Project) -> Result<Project>,
    ): Result<Unit> {
        if(_session.value == null)
            return Result.Failure("Something went wrong...")

        when(val result = transform(_session.value!!)) {
            is Result.Success -> {

                if(!saveUndo) {
                    _session.update { result.data }
                     return Result.Success(Unit)
                }

                val newUndos = _session
                    .value!!
                    .undos
                    .toMutableList()
                    .apply {
                        add(_session.value!!.elements)
                    }
                val newState = result.data.copy(
                    undos = newUndos
                )
                _session.update { newState }

                return Result.Success(Unit)
            }
            is Result.Failure -> return result
        }
    }

    private fun getSelectedElement(): DomainElement? {
        if(_session.value == null) return null
        if(_session.value!!.selectedElementIndex == null) return null

        return _session.value!!.elements[_session.value!!.selectedElementIndex!!]

    }
}