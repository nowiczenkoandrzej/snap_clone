package com.an.core_project.domain

import kotlinx.coroutines.flow.StateFlow

interface ProjectRepository {

    val session: StateFlow<Project?>

    suspend fun loadProject(id: Long)
    suspend fun saveProject()
    suspend fun loadProjectSummaries(): List<ProjectSummary>
    suspend fun startNewProject()
    suspend fun updateProject(
        saveUndo: Boolean = true,
        updatedProject: Project
    )

}


