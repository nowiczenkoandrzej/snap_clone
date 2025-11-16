package com.an.core_editor.data.model

import com.an.core_editor.data.edits.ImageEdit
import kotlinx.serialization.Serializable

@Serializable
sealed class DataImageEdit {
    abstract fun toDomain(): ImageEdit

    @Serializable
    data class ApplyFilter(val filterName: String): DataImageEdit() {
        override fun toDomain() = ImageEdit.ApplyFilter(filterName)
    }

    @Serializable
    data class CropImage(val cropRect: DataRect): DataImageEdit() {
        override fun toDomain() = ImageEdit.CropImage(cropRect.toDomain())
    }

    @Serializable
    data class DrawRubber(val paths: List<DataPathData>): DataImageEdit() {
        override fun toDomain() = ImageEdit.DrawRubber(paths.map { it.toDomain() })
    }

    @Serializable
    data class CutImage(val path: DataPathData): DataImageEdit() {
        override fun toDomain() = ImageEdit.CutImage(path.toDomain())
    }

    @Serializable
    data class DrawPaths(val paths: List<DataPathData>): DataImageEdit() {
        override fun toDomain() = ImageEdit.DrawPaths(paths.map { it.toDomain() })
    }

    @Serializable
    data class RemoveBackground(val mask: List<Boolean>): DataImageEdit() {
        override fun toDomain() = ImageEdit.RemoveBackground(mask.toBooleanArray())
    }
}
