package com.an.core_editor.presentation.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.core_editor.R
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.domain.model.Point
import com.an.core_editor.presentation.FontItem
import com.an.core_editor.presentation.barCodeFamily
import com.an.core_editor.presentation.cinzelFamily
import com.an.core_editor.presentation.dancingScriptFamily
import com.an.core_editor.presentation.gloriaFamily
import com.an.core_editor.presentation.goldmanFamily
import com.an.core_editor.presentation.indieFlowerFamily
import com.an.core_editor.presentation.lobsterTwoFamily
import com.an.core_editor.presentation.luckiestGuyFamily
import com.an.core_editor.presentation.meriendaFamily
import com.an.core_editor.presentation.michromaFamily
import com.an.core_editor.presentation.model.UiImageModel
import com.an.core_editor.presentation.model.UiStickerModel
import com.an.core_editor.presentation.model.UiTextModel
import com.an.core_editor.presentation.orbitronFamily
import com.an.core_editor.presentation.pacificoFamily
import com.an.core_editor.presentation.permanentMarkerFamily
import com.an.core_editor.presentation.presStartFamily
import com.an.core_editor.presentation.sacramentoFamily
import com.an.core_editor.presentation.silkScreenFamily

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

fun DomainColor.toCompose(): Color {
    return Color(
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

fun DomainTextModel.toUiTextModel(): UiTextModel {
    return UiTextModel(
        rotationAngle = this.rotationAngle,
        scale = this.scale,
        alpha = this.alpha,
        position = this.position.toOffset(),
        text = this.text,
        fontSize = this.fontSize,
        fontColor = this.fontColor.toCompose(),
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

fun List<Point>.toOffsetList(): List<Offset> {
    return this.map { point ->
        point.toOffset()
    }
}
