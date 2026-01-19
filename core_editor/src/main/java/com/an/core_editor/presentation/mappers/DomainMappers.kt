package com.an.core_editor.presentation.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.core_editor.data.edits.ApplyFilterBitmapEdit
import com.an.core_editor.data.edits.BitmapEdit
import com.an.core_editor.data.edits.CropImageBitmapEdit
import com.an.core_editor.data.edits.CutImageBitmapEdit
import com.an.core_editor.data.edits.DrawPathBitmapEdit
import com.an.core_editor.data.edits.DrawRubberBitmapEdit
import com.an.core_editor.data.edits.RemoveBackgroundBitmapEdit
import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.DomainImageEdit
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


