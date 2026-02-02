package com.an.core_project.domain

interface ProjectRepository {
    suspend fun load(id: Long): Project?
    suspend fun saveCurrent()
    suspend fun loadThumbnails(): List<ProjectSummary>

}
