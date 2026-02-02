package com.an.core_project.data

import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary
import com.an.core_saving.domain.ProjectDataSource

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val mapper: EntityMapper
): ProjectRepository {
    override suspend fun load(id: Long): Project? {
        val projectEntity = projectDataSource
            .getProjectById(id) ?: return null

        return return mapper.mapProjectEntityToDomainModel(projectEntity)
    }

    override suspend fun saveCurrent() {
        TODO("Not yet implemented")
    }

    override suspend fun loadThumbnails(): List<ProjectSummary> {
        TODO("Not yet implemented")
    }
}