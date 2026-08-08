package com.an.core_project.data

import android.graphics.Bitmap
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.ProjectDataSource
import com.an.feature_image_caching.BitmapSaver
import com.an.feature_image_rendering.ImageRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val imageRenderer: ImageRenderer,
    private val bitmapSaver: BitmapSaver,
    private val mapper: EntityMapper,
    private val elementSerializer: ElementSerializer,
): ProjectRepository {

    private val _session = MutableStateFlow<Project?>(null)
    override val session: StateFlow<Project?> = _session.asStateFlow()


    override suspend fun loadProject(id: Long) {
        val projectEntity = projectDataSource
            .getProjectById(id)

        if(projectEntity != null) {
            _session.value = mapper.mapProjectEntityToDomainModel(projectEntity)

            val imageModels = ArrayList<DomainImageModel>()

            _session.value?.elements?.forEach { element ->
                if(element is DomainImageModel)
                    imageModels.add(element)
            }

            imageRenderer.renderAndCache(
                imageModels
            )
        }

    }

    override suspend fun saveProject(
        thumbnail: Bitmap
    ) {

        val project = session.value ?: return


        val elementsSourcePath = elementSerializer.saveElements(project.elements)


        val thumbnailPath = bitmapSaver.saveBitmap(
            bitmap = thumbnail,
            qualityPercentage = 70
        )

        projectDataSource.insertProject(
            id = project.id,
            elementsSourcePath = elementsSourcePath,
            aspectRatio = project.aspectRatio.toDouble(),
            undosSourcePath = elementsSourcePath,
            lastChange = System.currentTimeMillis(),
            thumbnailSourcePath = thumbnailPath
        )
    }



    override suspend fun loadProjectSummaries(): List<ProjectSummary> {

        return projectDataSource
            .getProjectSummaries()
            .map { summary ->
                mapper.mapSummaryEntityToDomainModel(summary)
            }
    }

    override suspend fun startNewProject() {
        _session.value = Project()
    }

    override suspend fun updateProject(
        saveUndo: Boolean,
        project: Project,
    ) {


        val currentProject = _session.value

        if(currentProject == null) return

        val lastState = currentProject.elements


        val undos = if(saveUndo) {
            currentProject
                .undos
                .toMutableList()
                .apply {
                    add(lastState)
                }
                .toList()
        } else {
            currentProject.undos
        }

        _session.update { currentProject.copy(
            id = currentProject.id,
            elements = project.elements,
            aspectRatio = project.aspectRatio,
            undos = undos,
            lastChange = System.currentTimeMillis(),
            thumbNail = currentProject.thumbNail,
            selectedElementIndex = project.selectedElementIndex
        ) }

    }

    private fun getSelectedElement(): DomainElement? {
        if(_session.value == null) return null
        if(_session.value!!.selectedElementIndex == null) return null

        return _session.value!!.elements[_session.value!!.selectedElementIndex!!]

    }
}