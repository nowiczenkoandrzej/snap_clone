package com.an.core_project.domain

interface ProjectRepository {
    suspend fun load(id: Long): Project?
    suspend fun saveProject(project: Project)
    suspend fun loadThumbnails(): List<ProjectSummary>

}
