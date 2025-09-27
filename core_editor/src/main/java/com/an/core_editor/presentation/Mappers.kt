package com.an.core_editor.presentation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Point

fun DomainFontFamily.toFontItem(): FontItem {
    return when (this) {
        DomainFontFamily.DEFAULT -> FontItem(FontFamily.Default, "Default")
        DomainFontFamily.SERIF -> FontItem(FontFamily.Serif, "Serif")
        DomainFontFamily.CURSIVE -> FontItem(FontFamily.Cursive, "Cursive")
        DomainFontFamily.MONOSPACE -> FontItem(FontFamily.Monospace, "Monospace")
        DomainFontFamily.SANS_SERIF -> FontItem(FontFamily.SansSerif, "SansSerif")
        DomainFontFamily.LOBSTER_TWO -> FontItem(lobsterTwoFamily, "Lobster Two")
        DomainFontFamily.DANCING_SCRIPT -> FontItem(dancingScriptFamily, "Dancing Script")
        DomainFontFamily.GOLDMAN -> FontItem(goldmanFamily, "Goldman")
        DomainFontFamily.PACIFICO -> FontItem(pacificoFamily, "Pacifico")
        DomainFontFamily.MICHROMA -> FontItem(michromaFamily, "Michroma")
        DomainFontFamily.PERMANENT_MARKER -> FontItem(permanentMarkerFamily, "Permanent Marker")
        DomainFontFamily.LUCKIEST_GUY -> FontItem(luckiestGuyFamily, "Luckiest Guy")
        DomainFontFamily.INDIE_FLOWER -> FontItem(indieFlowerFamily, "Indie Flower")
        DomainFontFamily.ORBITRON -> FontItem(orbitronFamily, "Orbitron")
        DomainFontFamily.CINZEL -> FontItem(cinzelFamily, "Cinzel")
        DomainFontFamily.MERIENDA -> FontItem(meriendaFamily, "Merienda")
        DomainFontFamily.PRESS_START_2P -> FontItem(presStartFamily, "Press Start 2P")
        DomainFontFamily.GLORIA_HALLELUJAH -> FontItem(gloriaFamily, "Gloria Hallelujah")
        DomainFontFamily.SACRAMENTO -> FontItem(sacramentoFamily, "Sacramento")
        DomainFontFamily.SILKSCREEN -> FontItem(silkScreenFamily, "Silkscreen")
        DomainFontFamily.LIBRE_BARCODE_39 -> FontItem(barCodeFamily, "Libre Barcode 39")
    }
}

fun FontItem.toDomainFontFamily(): DomainFontFamily {
    val result = when (name) {
        "Default" -> DomainFontFamily.DEFAULT
        "Serif" -> DomainFontFamily.SERIF
        "Cursive" -> DomainFontFamily.CURSIVE
        "Monospace" -> DomainFontFamily.MONOSPACE
        "SansSerif" -> DomainFontFamily.SANS_SERIF
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
        else -> DomainFontFamily.DEFAULT
    }

    Log.d("TAG", "toDomainFontFamily: $result")

    return result
}

fun DomainColor.toComposeColor(): Color {
    return Color(
        red = this.red,
        blue = this.blue,
        green = this.green,
        alpha = this.alpha
    )
}

fun Color.toDomainColor(): DomainColor {
    return DomainColor(
        red = this.red,
        blue = this.blue,
        green = this.green,
        alpha = this.alpha
    )
}

fun Point.toOffset(): Offset {
    return Offset(
        x = this.x,
        y = this.y
    )
}

fun Offset.toPoint(): Point {
    return Point(
        x = this.x,
        y = this.y
    )
}

fun DomainTextModel.toUiTextModel(): UiTextModel {
    return UiTextModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        alpha = this.alpha,
        position = this.position.toOffset(),
        text = this.text,
        fontSize = this.fontSize,
        fontColor = this.fontColor.toComposeColor(),
        fontItem = this.fontFamily.toFontItem()
    )
}

fun DomainImageModel.toUiImageModel(cache: BitmapCache): UiImageModel {
    return UiImageModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        alpha = this.alpha,
        position = this.position.toOffset(),
        bitmap = cache.getEdited(this.id),
        currentFilter = this.currentFilter,
        version = this.version
    )
}


fun DomainStickerModel.toUiStickerModel(): UiStickerModel {
    return UiStickerModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        alpha = this.alpha,
        position = this.position.toOffset(),
        stickerPath = this.stickerPath,
    )


}


fun EditorState.toEditorUiState(cache: BitmapCache): EditorUiState {
    return EditorUiState(
        selectedElementIndex = this.selectedElementIndex,
        elements = this.elements.map { element ->
            val newElement = when(element) {
                is DomainTextModel -> element.toUiTextModel()
                is DomainImageModel -> element.toUiImageModel(cache)
                is DomainStickerModel -> element.toUiStickerModel()
            }
            newElement
        }
    )
}

fun List<Point>.toOffsetList(): List<Offset> {
    return this.map { point ->
        point.toOffset()
    }
}

fun List<Offset>.toPointList(): List<Point> {
    return this.map { offset ->
        offset.toPoint()
    }
}