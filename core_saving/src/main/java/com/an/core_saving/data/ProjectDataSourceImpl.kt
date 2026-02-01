package com.an.core_saving.data

import com.an.core_saving.ProjectDatabase
import com.an.core_saving.domain.ProjectDataSource
import com.an.coresaving.GetProjectSummaries
import com.an.coresaving.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProjectDataSourceImpl(
    db: ProjectDatabase
): ProjectDataSource {

    private val queries = db.projectEntityQueries

    override suspend fun getProjectById(id: Long): ProjectEntity? {
        return withContext(Dispatchers.IO) {
            queries.getProjectById(id).executeAsOneOrNull()
        }
    }

    override suspend fun getProjectSummaries(): List<GetProjectSummaries> {
        return withContext(Dispatchers.IO) {
            queries.getProjectSummaries().executeAsList()
        }
    }

    override suspend fun deleteProjectById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteProject(id)
        }
    }

    override suspend fun insertProject(
        id: Long?,
        elementsSourcePath: String?,
        aspectRatio: Double,
        undosSourcePath: String?,
        lastChange: Long,
        thumbnailSourcePath: String?,
    ) {
        withContext(Dispatchers.IO) {
            queries.insertProject(
                id,
                elementsSourcePath,
                aspectRatio,
                undosSourcePath,
                lastChange,
                thumbnailSourcePath
            )
        }
    }
}