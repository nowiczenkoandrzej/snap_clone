package com.an.facefilters.canvas.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.an.facefilters.R


@Composable
fun rememberFontList(): List<FontItem> {


    val lobsterTwoFamily = FontFamily(
        Font(R.font.lobstertwo_bold, FontWeight.Bold),
        Font(R.font.lobstertwo_regular, FontWeight.Normal),
        Font(R.font.lobstertwo_bolditalic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.lobstertwo_italic, FontWeight.Normal, FontStyle.Italic),
    )

    val dancingScriptFamily = FontFamily(
        Font(R.font.dancingscript_bold, FontWeight.Bold),
        Font(R.font.dancingscript_regular, FontWeight.Normal),
        Font(R.font.dancingscript_semibold, FontWeight.SemiBold),
        Font(R.font.dancingscript_medium, FontWeight.Medium),
    )

    val goldmanFamily = FontFamily(
        Font(R.font.goldman_bold, FontWeight.Bold),
        Font(R.font.goldman_regular, FontWeight.Normal),
    )
    val pacificoFamily = FontFamily(
        Font(R.font.pacifico_regular, FontWeight.Normal),
    )

    val michromaFamily = FontFamily(
        Font(R.font.michroma_regular, FontWeight.Normal),
    )
    val permanentMarkerFamily = FontFamily(
        Font(R.font.permanentmarker_regular, FontWeight.Normal),
    )
    val luckiestGuyFamily = FontFamily(
        Font(R.font.luckiestguy_regular, FontWeight.Normal),
    )

    val indieFlowerFamily = FontFamily(
        Font(R.font.indieflower_regular, FontWeight.Normal),
    )

    val orbitronFamily = FontFamily(
        Font(R.font.orbitron_bold, FontWeight.Bold),
        Font(R.font.orbitron_regular, FontWeight.Normal),
        Font(R.font.orbitron_black, FontWeight.Black),
        Font(R.font.orbitron_medium, FontWeight.Medium),
        Font(R.font.orbitron_semibold, FontWeight.SemiBold),
        Font(R.font.orbitron_extrabold, FontWeight.ExtraBold),
    )

    val cinzelFamily = FontFamily(
        Font(R.font.cinzel_bold, FontWeight.Bold),
        Font(R.font.cinzel_regular, FontWeight.Normal),
    )
    val meriendaFamily = FontFamily(
        Font(R.font.merienda_bold, FontWeight.Bold),
        Font(R.font.merienda_regular, FontWeight.Normal),
        Font(R.font.merienda_black, FontWeight.Black),
        Font(R.font.merienda_light, FontWeight.Light),
        Font(R.font.merienda_medium, FontWeight.Medium),
        Font(R.font.merienda_semibold, FontWeight.SemiBold),
        Font(R.font.merienda_extrabold, FontWeight.ExtraBold),
    )

    val presStartFamily = FontFamily(
        Font(R.font.pressstart2p_regular, FontWeight.Normal),
    )
    val gloriaFamily = FontFamily(
        Font(R.font.gloriahallelujah_regular, FontWeight.Normal),
    )
    val sacramentoFamily = FontFamily(
        Font(R.font.sacramento_regular, FontWeight.Normal),
    )
    val silkScreenFamily = FontFamily(
        Font(R.font.silkscreen_bold, FontWeight.Bold),
        Font(R.font.silkscreen_regular, FontWeight.Normal),
    )
    val barCodeFamily = FontFamily(
        Font(R.font.librebarcode39_regular, FontWeight.Normal)
    )

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

data class FontItem(
    val name: String,
    val fontFamily: FontFamily
)