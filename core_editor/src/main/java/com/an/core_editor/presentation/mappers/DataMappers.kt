package com.an.core_editor.presentation.mappers

import com.an.core_editor.data.edits.ApplyFilterBitmapEdit
import com.an.core_editor.data.edits.BitmapEdit
import com.an.core_editor.data.edits.CropImageBitmapEdit
import com.an.core_editor.data.edits.CutImageBitmapEdit
import com.an.core_editor.data.edits.DrawPathBitmapEdit
import com.an.core_editor.data.edits.DrawRubberBitmapEdit
import com.an.core_editor.data.edits.RemoveBackgroundBitmapEdit
import com.an.core_editor.data.model.DataColor
import com.an.core_editor.data.model.DataElement
import com.an.core_editor.data.model.DataImageEdit
import com.an.core_editor.data.model.DataImageModel
import com.an.core_editor.data.model.DataPathData
import com.an.core_editor.data.model.DataPoint
import com.an.core_editor.data.model.DataRect
import com.an.core_editor.data.model.DataStickerModel
import com.an.core_editor.data.model.DataTextModel
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point

fun DomainColor.toData(): DataColor {
    return DataColor(red, green, blue, alpha)
}

fun PathData.toData() = DataPathData(
    path = path.toDataPointList(),
    thickness = thickness,
    color = color.toData()
)

fun List<PathData>.toDataPathData(): List<DataPathData> {
    return this.map { it.toData() }
}

fun Point.toData(): DataPoint {
    return DataPoint(this.x, this.y)
}


fun List<Point>.toDataPointList(): List<DataPoint> {
    return this.map { it.toData()}
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

fun DomainImageEdit.toData(): DataImageEdit {
    return when (this) {
        is DomainImageEdit.CutImage -> DataImageEdit.CutImage(this.path.toData())
        is DomainImageEdit.ApplyFilter -> DataImageEdit.ApplyFilter(this.filterName)
        is DomainImageEdit.CropImage -> DataImageEdit.CropImage(
            DataRect(
                left = this.left,
                bottom = this.height,
                right = this.width,
                top = this.top
            )
        )
        is DomainImageEdit.DrawPaths -> DataImageEdit.DrawPaths(this.paths.toDataPathData())
        is DomainImageEdit.DrawRubber -> DataImageEdit.DrawRubber(this.paths.toDataPathData())
        is DomainImageEdit.RemoveBackground -> DataImageEdit.RemoveBackground(this.mask)
    }
}

fun List<DomainImageEdit>.toDataEdits(): List<DataImageEdit> {
    return this.map { it.toData() }
}

fun DomainImageModel.toData(): DataImageModel {
    return DataImageModel(
        id = this.id,
        imagePath = this.imagePath,
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toData(),
        alpha = this.alpha,
        edits = this.edits.toDataEdits(),
        currentFilter = this.currentFilter,
        version = this.version
    )
}

fun DomainStickerModel.toData() : DataStickerModel {
    return DataStickerModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toData(),
        alpha = this.alpha,
        stickerPath = this.stickerPath
    )
}

fun DomainTextModel.toData(): DataTextModel {
    return DataTextModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toData(),
        text = this.text,
        fontSize = this.fontSize,
        fontColor = this.fontColor.toData(),
        fontFamily = this.fontFamily.name,
        alpha = this.alpha
    )
}

fun DomainElement.toData(): DataElement {
    return when(this) {
        is DomainTextModel -> this.toData()
        is DomainStickerModel -> this.toData()
        is DomainImageModel -> this.toData()
    }
}

fun List<DomainElement>.toData(): List<DataElement> {
    return this.map { it.toData() }
}