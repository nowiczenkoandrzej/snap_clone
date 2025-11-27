package com.an.core_editor.presentation.mappers

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.core_editor.data.model.DataColor
import com.an.core_editor.data.model.DataElement
import com.an.core_editor.data.model.DataImageEdit
import com.an.core_editor.data.model.DataImageModel
import com.an.core_editor.data.model.DataPathData
import com.an.core_editor.data.model.DataPoint
import com.an.core_editor.data.model.DataStickerModel
import com.an.core_editor.data.model.DataTextModel
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point
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


fun DataColor.toDomain() = DomainColor(red, green, blue, alpha)

fun DataPathData.toDomain() = PathData(
    color = color.toDomain(),
    path = path.map { it.toDomain() },
    thickness = thickness
)

fun List<DataPoint>.toDomainPoints(): List<Point> {
    return this.map {
        it.toDomain()
    }
}

fun DataPoint.toDomain() = Point(x, y)

fun DataImageEdit.toDomain(): DomainImageEdit {
    return this.toDomain()
}

fun List<DataImageEdit>.toDomainEdits(): List<DomainImageEdit> {
    return this.map { it.toDomain() }
}

fun DataImageModel.toDomain(): DomainImageModel {
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

fun DataStickerModel.toDomain(): DomainStickerModel {
    return DomainStickerModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        position = this.position.toDomain(),
        alpha = this.alpha,
        stickerPath = this.stickerPath
    )
}

fun DataTextModel.toDomain(): DomainTextModel {
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

fun List<DataElement>.toDomainElements(): List<DomainElement> {
    return this.map {
        when(it) {
            is DataTextModel -> it.toDomain()
            is DataImageModel -> it.toDomain()
            is DataStickerModel -> it.toDomain()
        }
    }
}