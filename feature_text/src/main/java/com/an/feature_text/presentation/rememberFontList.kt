package com.an.feature_text.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import com.an.core_editor.R
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
            fontFamily = lobsterTwoFamily,
            name = "Lobster Two",
            fontResId = R.font.lobstertwo_regular
        ),

        FontItem(
            fontFamily = dancingScriptFamily,
            name = "Dancing Script",
            fontResId = R.font.dancingscript_regular
        ),
        FontItem(
            fontFamily = goldmanFamily,
            name = "Goldman",
            fontResId = R.font.goldman_regular
        ),
        FontItem(
            fontFamily = pacificoFamily,
            name = "Pacifico",
            fontResId = R.font.pacifico_regular
        ),

        FontItem(
            fontFamily = michromaFamily,
            name = "Michroma",
            fontResId = R.font.michroma_regular
        ),

        FontItem(
            fontFamily = permanentMarkerFamily,
            name = "Permanent Marker",
            fontResId = R.font.permanentmarker_regular
        ),

        FontItem(
            fontFamily = luckiestGuyFamily,
            name = "Luckiest Guy",
            fontResId = R.font.luckiestguy_regular
        ),

        FontItem(
            fontFamily = indieFlowerFamily,
            name = "Indie Flower",
            fontResId = R.font.indieflower_regular
        ),
        FontItem(
            fontFamily = orbitronFamily,
            name = "Orbitron",
            fontResId = R.font.orbitron_regular
        ),
        FontItem(
            fontFamily = cinzelFamily,
            name = "Cinzel",
            fontResId = R.font.cinzel_regular
        ),
        FontItem(
            fontFamily = meriendaFamily,
            name = "Merienda",
            fontResId = R.font.merienda_regular
        ),
        FontItem(
            fontFamily = presStartFamily,
            name = "Press Start 2P",
            fontResId = R.font.pressstart2p_regular
        ),
        FontItem(
            fontFamily = gloriaFamily,
            name = "Gloria Hallelujah",
            fontResId = R.font.gloriahallelujah_regular
        ),
        FontItem(
            fontFamily = sacramentoFamily,
            name = "Sacramento",
            fontResId = R.font.sacramento_regular
        ),
        FontItem(
            fontFamily = silkScreenFamily,
            name = "Silkscreen",
            fontResId = R.font.silkscreen_regular
        ),
        FontItem(
            fontFamily = barCodeFamily,
            name = "Libre Barcode 39",
            fontResId = R.font.librebarcode39_regular
        ),
    )

    return remember { fonts }
}