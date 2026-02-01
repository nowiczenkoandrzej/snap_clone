package com.an.core_saving.domain

import com.an.coresaving.GetProjectSummaries
import com.an.coresaving.ProjectEntity

interface ProjectDataSource {

    suspend fun getProjectById(id: Long): ProjectEntity?
    suspend fun getProjectSummaries(): List<GetProjectSummaries>
    suspend fun deleteProjectById(id: Long)
    suspend fun insertProject(
        id: Long? = null,
        elementsSourcePath: String?,
        aspectRatio: Double,
        undosSourcePath: String?,
        lastChange: Long,
        thumbnailSourcePath: String?,
    )

}