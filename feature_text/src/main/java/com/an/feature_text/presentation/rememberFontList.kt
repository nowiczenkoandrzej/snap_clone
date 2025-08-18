package com.an.feature_text.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
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
import com.an.core_editor.presentation.orbitronFamily
import com.an.core_editor.presentation.pacificoFamily
import com.an.core_editor.presentation.permanentMarkerFamily
import com.an.core_editor.presentation.presStartFamily
import com.an.core_editor.presentation.sacramentoFamily
import com.an.core_editor.presentation.silkScreenFamily

@Composable
fun rememberFontList(): List<FontItem> {

    val fonts = listOf(
        FontItem(
            fontFamily = FontFamily.Default,
            name = "Default"
        ),
        FontItem(
            fontFamily = FontFamily.Serif,
            name = "Serif"
        ),
        FontItem(
            fontFamily = FontFamily.Cursive,
            name = "Cursive"
        ),
        FontItem(
            fontFamily = FontFamily.Monospace,
            name = "Monospace"
        ),
        FontItem(
            fontFamily = FontFamily.SansSerif,
            name = "SansSerif"
        ),
        FontItem(
            fontFamily = lobsterTwoFamily,
            name = "Lobster Two"
        ),

        FontItem(
            fontFamily = dancingScriptFamily,
            name = "Dancing Script"
        ),
        FontItem(
            fontFamily = goldmanFamily,
            name = "Goldman"
        ),
        FontItem(
            fontFamily = pacificoFamily,
            name = "Pacifico"
        ),

        FontItem(
            fontFamily = michromaFamily,
            name = "Michroma"
        ),

        FontItem(
            fontFamily = permanentMarkerFamily,
            name = "Permanent Marker"
        ),

        FontItem(
            fontFamily = luckiestGuyFamily,
            name = "Luckiest Guy"
        ),

        FontItem(
            fontFamily = indieFlowerFamily,
            name = "Indie Flower"
        ),
        FontItem(
            fontFamily = orbitronFamily,
            name = "Orbitron"
        ),
        FontItem(
            fontFamily = cinzelFamily,
            name = "Cinzel"
        ),
        FontItem(
            fontFamily = meriendaFamily,
            name = "Merienda"
        ),
        FontItem(
            fontFamily = presStartFamily,
            name = "Press Start 2P"
        ),
        FontItem(
            fontFamily = gloriaFamily,
            name = "Gloria Hallelujah"
        ),
        FontItem(
            fontFamily = sacramentoFamily,
            name = "Sacramento"
        ),
        FontItem(
            fontFamily = silkScreenFamily,
            name = "Silkscreen"
        ),
        FontItem(
            fontFamily = barCodeFamily,
            name = "Libre Barcode 39"
        ),
    )

    return remember { fonts }
}