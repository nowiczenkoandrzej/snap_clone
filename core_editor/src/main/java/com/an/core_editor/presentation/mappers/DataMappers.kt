package com.an.core_editor.presentation.mappers

import com.an.core_editor.data.edits.ApplyFilterBitmapEdit
import com.an.core_editor.data.edits.BitmapEdit
import com.an.core_editor.data.edits.CropImageBitmapEdit
import com.an.core_editor.data.edits.CutImageBitmapEdit
import com.an.core_editor.data.edits.DrawPathBitmapEdit
import com.an.core_editor.data.edits.DrawRubberBitmapEdit
import com.an.core_editor.data.edits.RemoveBackgroundBitmapEdit
import com.an.core_editor.data.model.SerializedColor
import com.an.core_editor.data.model.SerializedElement
import com.an.core_editor.data.model.SerializedImageEdit
import com.an.core_editor.data.model.SerializedImageModel
import com.an.core_editor.data.model.SerializedPathData
import com.an.core_editor.data.model.SerializedPoint
import com.an.core_editor.data.model.SerializedRect
import com.an.core_editor.data.model.SerializedStickerModel
import com.an.core_editor.data.model.SerializedTextModel
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point

fun DomainColor.toData(): SerializedColor {
    return SerializedColor(red, green, blue, alpha)
}

fun PathData.toData() = SerializedPathData(
    path = path.toDataPointList(),
    thickness = thickness,
    color = color.toData()
)

fun List<PathData>.toDataPathData(): List<SerializedPathData> {
    return this.map { it.toData() }
}

fun Point.toData(): SerializedPoint {
    return SerializedPoint(this.x, this.y)
}


fun List<Point>.toDataPointList(): List<SerializedPoint> {
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

fun DomainImageEdit.toData(): SerializedImageEdit {
    return when (this) {
        is DomainImageEdit.CutImage -> SerializedImageEdit.CutImage(this.path.toData())
        is DomainImageEdit.ApplyFilter -> SerializedImageEdit.ApplyFilter(this.filterName)
        is DomainImageEdit.CropImage -> SerializedImageEdit.CropImage(
            SerializedRect(
                left = this.left,
                bottom = this.height,
                right = this.width,
                top = this.top
            )
        )
        is DomainImageEdit.DrawPaths -> SerializedImageEdit.DrawPaths(this.paths.toDataPathData())
        is DomainImageEdit.DrawRubber -> SerializedImageEdit.DrawRubber(this.paths.toDataPathData())
        is DomainImageEdit.RemoveBackground -> SerializedImageEdit.RemoveBackground(this.mask)
    }
}

fun List<DomainImageEdit>.toDataEdits(): List<SerializedImageEdit> {
    return this.map { it.toData() }
}

fun DomainImageModel.toData(): SerializedImageModel {
    return SerializedImageModel(
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

fun DomainStickerModel.toData() : SerializedStickerModel {
    return SerializedStickerModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toData(),
        alpha = this.alpha,
        stickerPath = this.stickerPath
    )
}

fun DomainTextModel.toData(): SerializedTextModel {
    return SerializedTextModel(
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

fun DomainElement.toData(): SerializedElement {
    return when(this) {
        is DomainTextModel -> this.toData()
        is DomainStickerModel -> this.toData()
        is DomainImageModel -> this.toData()
    }
}

fun List<DomainElement>.toData(): List<SerializedElement> {
    return this.map { it.toData() }
}