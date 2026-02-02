package com.an.core_project.data

import com.an.core_project.domain.Project
import com.an.core_saving.domain.BitmapSaver
import com.an.core_saving.domain.ElementSerializer
import com.an.coresaving.ProjectEntity

class EntityMapper(
    private val bitmapSaver: BitmapSaver,
    private val elementSerializer: ElementSerializer,
) {

    suspend fun mapProjectEntityToDomainModel(
        entity: ProjectEntity
    ): Project {


        val elements = entity
            .elementsSourcePath
            ?.let   { path ->
                elementSerializer.loadElements(path)
            } ?: emptyList()

        return Project(
            id = entity.id,
            elements = elements,
            aspectRatio = entity.aspectRatio.toFloat(),
            undos = emptyList(),
            lastChange = entity.lastChange,
            thumbNail = entity.thumbnailSourcePath,
            selectedElementIndex = null
        )
    }





}