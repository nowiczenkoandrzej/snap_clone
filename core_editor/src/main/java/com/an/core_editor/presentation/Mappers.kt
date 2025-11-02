package com.an.core_editor.presentation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.an.core_editor.R
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.EditorState
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Point

fun DomainFontFamily.toFontItem(): FontItem {
    return when (this) {
        DomainFontFamily.LOBSTER_TWO -> FontItem(lobsterTwoFamily, "Lobster Two", R.font.lobstertwo_regular)
        DomainFontFamily.DANCING_SCRIPT -> FontItem(dancingScriptFamily, "Dancing Script", R.font.dancingscript_regular)
        DomainFontFamily.GOLDMAN -> FontItem(goldmanFamily, "Goldman", R.font.goldman_regular)
        DomainFontFamily.PACIFICO -> FontItem(pacificoFamily, "Pacifico", R.font.pacifico_regular)
        DomainFontFamily.MICHROMA -> FontItem(michromaFamily, "Michroma", R.font.michroma_regular)
        DomainFontFamily.PERMANENT_MARKER -> FontItem(permanentMarkerFamily, "Permanent Marker", R.font.permanentmarker_regular)
        DomainFontFamily.LUCKIEST_GUY -> FontItem(luckiestGuyFamily, "Luckiest Guy", R.font.luckiestguy_regular)
        DomainFontFamily.INDIE_FLOWER -> FontItem(indieFlowerFamily, "Indie Flower", R.font.indieflower_regular)
        DomainFontFamily.ORBITRON -> FontItem(orbitronFamily, "Orbitron", R.font.orbitron_regular)
        DomainFontFamily.CINZEL -> FontItem(cinzelFamily, "Cinzel", R.font.cinzel_regular)
        DomainFontFamily.MERIENDA -> FontItem(meriendaFamily, "Merienda", R.font.merienda_regular)
        DomainFontFamily.PRESS_START_2P -> FontItem(presStartFamily, "Press Start 2P", R.font.pressstart2p_regular)
        DomainFontFamily.GLORIA_HALLELUJAH -> FontItem(gloriaFamily, "Gloria Hallelujah", R.font.gloriahallelujah_regular)
        DomainFontFamily.SACRAMENTO -> FontItem(sacramentoFamily, "Sacramento", R.font.sacramento_regular)
        DomainFontFamily.SILKSCREEN -> FontItem(silkScreenFamily, "Silkscreen", R.font.silkscreen_regular)
        DomainFontFamily.LIBRE_BARCODE_39 -> FontItem(barCodeFamily, "Libre Barcode 39", R.font.librebarcode39_regular)
    }
}

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

fun DomainImageModel.toUiImageModel(imageRenderer: ImageRenderer): UiImageModel {
    return UiImageModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        alpha = this.alpha,
        position = this.position.toOffset(),
        bitmap = imageRenderer.render(this),
//        currentFilter = this.currentFilter,
        //version = this.version
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


fun EditorState.toEditorUiState(imageRenderer: ImageRenderer): EditorUiState {
    return EditorUiState(
        selectedElementIndex = this.selectedElementIndex,
        elements = this.elements.map { element ->
            val newElement = when(element) {
                is DomainTextModel -> element.toUiTextModel()
                is DomainImageModel -> element.toUiImageModel(imageRenderer)
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
