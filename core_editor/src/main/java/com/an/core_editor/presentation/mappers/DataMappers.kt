package com.an.core_editor.presentation.mappers

import com.an.core_editor.data.edits.ApplyFilterBitmapEdit
import com.an.core_editor.data.edits.BitmapEdit
import com.an.core_editor.data.edits.CropImageBitmapEdit
import com.an.core_editor.data.edits.CutImageBitmapEdit
import com.an.core_editor.data.edits.DrawPathBitmapEdit
import com.an.core_editor.data.edits.DrawRubberBitmapEdit
import com.an.core_editor.data.edits.ImageEdit
import com.an.core_editor.data.edits.RemoveBackgroundBitmapEdit
import com.an.core_editor.data.model.DataColor
import com.an.core_editor.data.model.DataImageEdit
import com.an.core_editor.data.model.DataPathData
import com.an.core_editor.data.model.DataPoint
import com.an.core_editor.data.model.toData
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point

fun DomainColor.toData(): DataColor {
    return DataColor(red, green, blue, alpha)
}

fun PathData.toData() = DataPathData(
    path = path.toData(),
    thickness = thickness,
    color = color.toData()
)



fun List<Point>.toData(): List<DataPoint> {
    return this.map {
        DataPoint(it.x, it.y)
    }
}

fun DomainImageEdit.toBitmapEdit(): BitmapEdit {
    return when (this) {
        is DomainImageEdit.ApplyFilter -> ApplyFilterBitmapEdit(filterName)
        is DomainImageEdit.CropImage -> CropImageBitmapEdit(left, top, width, height)
        is DomainImageEdit.DrawRubber -> DrawRubberBitmapEdit(paths)
        is DomainImageEdit.DrawPaths -> DrawPathBitmapEdit(paths)
        is DomainImageEdit.CutImage -> CutImageBitmapEdit(path)
        is DomainImageEdit.RemoveBackground -> RemoveBackgroundBitmapEdit(mask)
    }
}