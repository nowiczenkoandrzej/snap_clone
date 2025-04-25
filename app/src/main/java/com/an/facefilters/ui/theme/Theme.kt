package com.an.facefilters.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


// Light
val md_theme_light_primary = Color(0xFF007A78)
val md_theme_light_onPrimary = Color.White
val md_theme_light_primaryContainer = Color(0xFF6FD4D1)
val md_theme_light_onPrimaryContainer = Color(0xFF00201F)

val md_theme_light_secondary = Color(0xFF005A9E)
val md_theme_light_onSecondary = Color.White
val md_theme_light_secondaryContainer = Color(0xFFB3D9FF)
val md_theme_light_onSecondaryContainer = Color(0xFF001E36)

val md_theme_light_tertiary = Color(0xFF00867E)
val md_theme_light_onTertiary = Color.White
val md_theme_light_tertiaryContainer = Color(0xFFA0E6DC)
val md_theme_light_onTertiaryContainer = Color(0xFF00201C)

val md_theme_light_background = Color(0xFFF6F6F6)
val md_theme_light_onBackground = Color(0xFF121212)
val md_theme_light_surface = Color(0xFFFFFFFF)
val md_theme_light_onSurface = Color(0xFF121212)

val md_theme_light_surfaceVariant = Color(0xFFE0F0EF)
val md_theme_light_onSurfaceVariant = Color(0xFF2E4D4D)
val md_theme_light_outline = Color(0xFF5A7E7E)

val md_theme_light_error = Color(0xFFB3261E)
val md_theme_light_onError = Color.White
val md_theme_light_errorContainer = Color(0xFFF9DEDC)
val md_theme_light_onErrorContainer = Color(0xFF410E0B)

// Dark
val md_theme_dark_primary = Color(0xFF4FD1CE)
val md_theme_dark_onPrimary = Color(0xFF003332)
val md_theme_dark_primaryContainer = Color(0xFF00514F)
val md_theme_dark_onPrimaryContainer = Color(0xFF99F0ED)

val md_theme_dark_secondary = Color(0xFF8EC2FF)
val md_theme_dark_onSecondary = Color(0xFF00325C)
val md_theme_dark_secondaryContainer = Color(0xFF004A87)
val md_theme_dark_onSecondaryContainer = Color(0xFFD6E9FF)

val md_theme_dark_tertiary = Color(0xFF5FE1D4)
val md_theme_dark_onTertiary = Color(0xFF00332E)
val md_theme_dark_tertiaryContainer = Color(0xFF00514A)
val md_theme_dark_onTertiaryContainer = Color(0xFFAAFAF2)

val md_theme_dark_background = Color(0xFF101C1C)
val md_theme_dark_onBackground = Color(0xFFE0F0F0)
val md_theme_dark_surface = Color(0xFF172222)
val md_theme_dark_onSurface = Color(0xFFE0F0F0)

val md_theme_dark_surfaceVariant = Color(0xFF324B4B)
val md_theme_dark_onSurfaceVariant = Color(0xFFBFCFCF)
val md_theme_dark_outline = Color(0xFF7DA0A0)

val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)



val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,

    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,

    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,

    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,

    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,

    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer
)

val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,

    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,

    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,

    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,

    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,

    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer
)

@Composable
fun FaceFiltersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    forceDarkMode: Boolean? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        forceDarkMode == true -> DarkColorScheme
        forceDarkMode == false -> LightColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}