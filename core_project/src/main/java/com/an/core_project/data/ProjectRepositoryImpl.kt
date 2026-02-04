package com.an.core_project.data

import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary
import com.an.core_saving.domain.BitmapSaver
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.ProjectDataSource

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val mapper: EntityMapper,
    private val elementSerializer: ElementSerializer,
    private val bitmapSaver: BitmapSaver
): ProjectRepository {
    override suspend fun load(id: Long): Project? {
        val projectEntity = projectDataSource
            .getProjectById(id) ?: return null

        return mapper.mapProjectEntityToDomainModel(projectEntity)
    }

    override suspend fun saveProject(project: Project) {


        projectDataSource.insertProject(
            id = project.id,
            elementsSourcePath = project,
            aspectRatio = TODO(),
            undosSourcePath = TODO(),
            lastChange = TODO(),
            thumbnailSourcePath = TODO()
        )
    }

    override suspend fun loadThumbnails(): List<ProjectSummary> {
        TODO("Not yet implemented")
    }
}