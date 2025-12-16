package com.an.core_editor.data.model

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.presentation.mappers.toDomain
import kotlinx.serialization.Serializable

@Serializable
sealed class SerializedImageEdit {
    abstract fun toDomain(): DomainImageEdit

    @Serializable
    data class ApplyFilter(val filterName: String): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.ApplyFilter(filterName)
    }

    @Serializable
    data class CropImage(val cropRect: SerializedRect): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.CropImage(
            left = cropRect.left,
            top = cropRect.top,
            width = cropRect.right,
            height = cropRect.bottom
        )
    }

    @Serializable
    data class DrawRubber(val paths: List<SerializedPathData>): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.DrawRubber(paths.map { it.toDomain() })
    }

    @Serializable
    data class CutImage(val path: SerializedPathData): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.CutImage(path.toDomain())
    }

    @Serializable
    data class DrawPaths(val paths: List<SerializedPathData>): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.DrawPaths(paths.map { it.toDomain() })
    }

    @Serializable
    data class RemoveBackground(val mask: BooleanArray): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.RemoveBackground(mask)
    }
}
