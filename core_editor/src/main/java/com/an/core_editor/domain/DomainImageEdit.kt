package com.an.core_editor.domain

import com.an.core_editor.domain.model.PathData

sealed interface DomainImageEdit {

    data class ApplyFilter(val filterName: String) : DomainImageEdit

    data class CropImage(
        val left: Float,
        val top: Float,
        val width: Float,
        val height: Float
    ) : DomainImageEdit

    data class DrawRubber(val paths: List<PathData>) : DomainImageEdit

    data class DrawPaths(val paths: List<PathData>) : DomainImageEdit

    data class CutImage(val path: PathData) : DomainImageEdit

    data class RemoveBackground(val mask: BooleanArray) : DomainImageEdit
}
