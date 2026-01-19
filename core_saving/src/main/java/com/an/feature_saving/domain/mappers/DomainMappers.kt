package com.an.feature_saving.domain.mappers

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Project
import com.an.feature_saving.domain.model.SerializedColor
import com.an.feature_saving.domain.model.SerializedElement
import com.an.feature_saving.domain.model.SerializedImageEdit
import com.an.feature_saving.domain.model.SerializedImageModel
import com.an.feature_saving.domain.model.SerializedPathData
import com.an.feature_saving.domain.model.SerializedPoint
import com.an.feature_saving.domain.model.SerializedProject
import com.an.feature_saving.domain.model.SerializedStickerModel
import com.an.feature_saving.domain.model.SerializedTextModel

fun SerializedColor.toDomain() = DomainColor(red, green, blue, alpha)

fun SerializedPathData.toDomain() = PathData(
    color = color.toDomain(),
    path = path.map { it.toDomain() },
    thickness = thickness
)

fun List<SerializedPoint>.toDomainPoints(): List<Point> {
    return this.map {
        it.toDomain()
    }
}

fun SerializedPoint.toDomain() = Point(x, y)

fun SerializedImageEdit.toDomain(): DomainImageEdit {
    return this.toDomain()
}

fun List<SerializedImageEdit>.toDomainEdits(): List<DomainImageEdit> {
    return this.map { it.toDomain() }
}

fun SerializedImageModel.toDomain(): DomainImageModel {
    return DomainImageModel(
        id = this.id,
        imagePath = this.imagePath,
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toDomain(),
        alpha = this.alpha,
        edits = this.edits.toDomainEdits(),
        currentFilter = this.currentFilter,
        version = this.version
    )
}

fun SerializedStickerModel.toDomain(): DomainStickerModel {
    return DomainStickerModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toDomain(),
        alpha = this.alpha,
        stickerPath = this.stickerPath
    )
}

fun SerializedTextModel.toDomain(): DomainTextModel {
    return DomainTextModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toDomain(),
        text = this.text,
        fontSize = this.fontSize,
        fontColor = this.fontColor.toDomain(),
        fontFamily = DomainFontFamily.valueOf(this.fontFamily),
        alpha = this.alpha
    )
}

fun List<SerializedElement>.toDomainElements(): List<DomainElement> {
    return this.map {
        when(it) {
            is SerializedTextModel -> it.toDomain()
            is SerializedImageModel -> it.toDomain()
            is SerializedStickerModel -> it.toDomain()
        }
    }
}

fun SerializedProject.toDomain(): Project {

    val undos = this.undos.map { serialized ->
        EditorState(
            elements = serialized.elements.toDomainElements(),
            selectedElementIndex = serialized.selectedElementIndex
        )
    }

    return Project(
        elements = this.elements.toDomainElements(),
        aspectRatio = this.aspectRatio,
        undos = undos
    )
}