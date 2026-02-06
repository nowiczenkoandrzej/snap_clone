package com.an.core_project.data

import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.ProjectDataSource
import com.an.feature_image_caching.BitmapSaver
import com.an.feature_image_rendering.ImageRenderer

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val mapper: EntityMapper,
    private val elementSerializer: ElementSerializer,
    private val bitmapSaver: BitmapSaver,
    private val imageRenderer: ImageRenderer
): ProjectRepository {
    override suspend fun load(id: Long): Project? {
        val projectEntity = projectDataSource
            .getProjectById(id) ?: return null

        return mapper.mapProjectEntityToDomainModel(projectEntity)
    }

    override suspend fun saveProject(project: Project) {

        val elementsSourcePath = elementSerializer.saveElements(project.elements)
        val thumbnailBitmap = imageRenderer.render(project.elements)

        projectDataSource.insertProject(
            id = project.id,
            elementsSourcePath = elementsSourcePath,
            aspectRatio = project.aspectRatio.toDouble(),
            undosSourcePath = "",
            lastChange = project.lastChange,
            thumbnailSourcePath =
        )
    }

    override suspend fun loadThumbnails(): List<ProjectSummary> {
        TODO("Not yet implemented")
    }
}