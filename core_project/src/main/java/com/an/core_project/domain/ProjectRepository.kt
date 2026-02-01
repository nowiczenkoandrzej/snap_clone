package com.an.core_project.domain

interface ProjectRepository {
    fun load(id: Long): Project
    fun saveCurrent()
    fun loadThumbnails(): List<ProjectSummary>

}
