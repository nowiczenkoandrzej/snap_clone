package com.an.core_editor.presentation.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.core_editor.data.model.SerializedColor
import com.an.core_editor.data.model.SerializedElement
import com.an.core_editor.data.model.SerializedImageEdit
import com.an.core_editor.data.model.SerializedImageModel
import com.an.core_editor.data.model.SerializedPathData
import com.an.core_editor.data.model.SerializedPoint
import com.an.core_editor.data.model.SerializedProject
import com.an.core_editor.data.model.SerializedStickerModel
import com.an.core_editor.data.model.SerializedTextModel
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
import com.an.core_editor.presentation.FontItem


fun FontItem.toDomainFontFamily(): DomainFontFamily {
    val result = when (name) {

        "Lobster Two" -> DomainFontFamily.LOBSTER_TWO
        "Dancing Script" -> DomainFontFamily.DANCING_SCRIPT
        "Goldman" -> DomainFontFamily.GOLDMAN
        "Pacifico" -> DomainFontFamily.PACIFICO
        "Michroma" -> DomainFontFamily.MICHROMA
        "Permanent Marker" -> DomainFontFamily.PERMANENT_MARKER
        "Luckiest Guy" -> DomainFontFamily.LUCKIEST_GUY
        "Indie Flower" -> DomainFontFamily.INDIE_FLOWER
        "Orbitron" -> DomainFontFamily.ORBITRON
        "Cinzel" -> DomainFontFamily.CINZEL
        "Merienda" -> DomainFontFamily.MERIENDA
        "Press Start 2P" -> DomainFontFamily.PRESS_START_2P
        "Gloria Hallelujah" -> DomainFontFamily.GLORIA_HALLELUJAH
        "Sacramento" -> DomainFontFamily.SACRAMENTO
        "Silkscreen" -> DomainFontFamily.SILKSCREEN
        "Libre Barcode 39" -> DomainFontFamily.LIBRE_BARCODE_39

        else -> DomainFontFamily.LOBSTER_TWO
    }


    return result
}



fun Color.toDomain(): DomainColor {
    return DomainColor(
        red = this.red,
        blue = this.blue,
        green = this.green,
        alpha = this.alpha
    )
}



fun Offset.toPoint(): Point {
    return Point(
        x = this.x,
        y = this.y
    )
}



fun List<Offset>.toPointList(): List<Point> {
    return this.map { offset ->
        offset.toPoint()
    }
}


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