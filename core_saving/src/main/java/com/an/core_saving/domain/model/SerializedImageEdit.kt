package com.an.core_saving.domain.model

import com.an.core_editor.domain.DomainImageEdit
import com.an.core_saving.data.mappers.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SerializedImageEdit {
    abstract fun toDomain(): DomainImageEdit

    @Serializable
    @SerialName("applyFilter")
    data class ApplyFilter(val filterName: String): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.ApplyFilter(filterName)
    }

    @Serializable
    @SerialName("cropImage")
    data class CropImage(val cropRect: SerializedRect): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.CropImage(
            left = cropRect.left,
            top = cropRect.top,
            width = cropRect.right,
            height = cropRect.bottom
        )
    }

    @Serializable
    @SerialName("drawRubber")
    data class DrawRubber(val paths: List<SerializedPathData>): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.DrawRubber(paths.map { it.toDomain() })
    }

    @Serializable
    @SerialName("cutImage")
    data class CutImage(val path: SerializedPathData): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.CutImage(path.toDomain())
    }

    @Serializable
    @SerialName("drawPaths")
    data class DrawPaths(val paths: List<SerializedPathData>): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.DrawPaths(paths.map { it.toDomain() })
    }

    @Serializable
    @SerialName("removeBackground")
    data class RemoveBackground(val mask: BooleanArray): SerializedImageEdit() {
        override fun toDomain() = DomainImageEdit.RemoveBackground(mask)
    }
}