package com.an.core_saving.data.mappers

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point
import com.an.core_saving.domain.model.SerializedColor
import com.an.core_saving.domain.model.SerializedElement
import com.an.core_saving.domain.model.SerializedImageEdit
import com.an.core_saving.domain.model.SerializedImageModel
import com.an.core_saving.domain.model.SerializedPathData
import com.an.core_saving.domain.model.SerializedPoint
import com.an.core_saving.domain.model.SerializedRect
import com.an.core_saving.domain.model.SerializedStickerModel
import com.an.core_saving.domain.model.SerializedTextModel


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
