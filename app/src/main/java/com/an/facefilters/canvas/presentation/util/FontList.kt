package com.an.facefilters.canvas.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.an.facefilters.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


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
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Lobster Two"), fontProvider = provider)
            ),
            name = "Lobster Two"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Bebas Neue"), fontProvider = provider)
            ),
            name = "Bebas Neue"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Bungee"), fontProvider = provider)
            ),
            name = "Bungee"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Dancing Script"), fontProvider = provider)
            ),
            name = "Dancing Script"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Goldman"), fontProvider = provider)
            ),
            name = "Goldman"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Pacifico"), fontProvider = provider)
            ),
            name = "Pacifico"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Caveat"), fontProvider = provider)
            ),
            name = "Caveat"
        ),

        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Michroma"), fontProvider = provider)
            ),
            name = "Michroma"
        ),

        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Permanent Marker"), fontProvider = provider)
            ),
            name = "Permanent Marker"
        ),

        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Luckiest Guy"), fontProvider = provider)
            ),
            name = "Luckiest Guy"
        ),

        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Shadows Into Light"), fontProvider = provider)
            ),
            name = "Shadows Into Light"
        ),

        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Indie Flower"), fontProvider = provider)
            ),
            name = "Indie Flower"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Orbitron"), fontProvider = provider)
            ),
            name = "Orbitron"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Cinzel"), fontProvider = provider)
            ),
            name = "Cinzel"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Merienda"), fontProvider = provider)
            ),
            name = "Merienda"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Bangers"), fontProvider = provider)
            ),
            name = "Bangers"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Press Start 2P"), fontProvider = provider)
            ),
            name = "Press Start 2P"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Gloria Hallelujah"), fontProvider = provider)
            ),
            name = "Gloria Hallelujah"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Sacramento"), fontProvider = provider)
            ),
            name = "Sacramento"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Silkscreen"), fontProvider = provider)
            ),
            name = "Silkscreen"
        ),
        FontItem(
            fontFamily = FontFamily(
                Font(googleFont = GoogleFont("Libre Barcode 39"), fontProvider = provider)
            ),
            name = "Libre Barcode 39"
        ),


    )

    return remember { fonts }

}

data class FontItem(
    val name: String,
    val fontFamily: FontFamily
)