package com.an.facefilters.ui.theme

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



private val md_theme_light_primary = Color(0xFF4A4A4A) // Warm charcoal
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFF5F5F0) // Warm off-white
private val md_theme_light_onPrimaryContainer = Color(0xFF2A2A2A)

private val md_theme_light_secondary = Color(0xFF6B6B6B) // Medium grey
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFFAFAF8) // Cream white
private val md_theme_light_onSecondaryContainer = Color(0xFF3A3A3A)

private val md_theme_light_background = Color(0xFFFFFEFC) // Warm white
private val md_theme_light_onBackground = Color(0xFF1F1F1F)
private val md_theme_light_surface = Color(0xFFFFFFFF)
private val md_theme_light_onSurface = Color(0xFF1F1F1F)

private val md_theme_light_surfaceVariant = Color(0xFFF8F7F5) // Warm light grey
private val md_theme_light_onSurfaceVariant = Color(0xFF525252)
private val md_theme_light_outline = Color(0xFFD0CFC8)

private val md_theme_light_error = Color(0xFFB71C1C)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_errorContainer = Color(0xFFFFEBEE)
private val md_theme_light_onErrorContainer = Color(0xFF660011)

// Dark theme - Warm Charcoal
private val md_theme_dark_primary = Color(0xFFB8B8B8) // Light warm grey
private val md_theme_dark_onPrimary = Color(0xFF2A2A2A)
private val md_theme_dark_primaryContainer = Color(0xFF4A4A4A) // Charcoal container
private val md_theme_dark_onPrimaryContainer = Color(0xFFF5F5F0)

private val md_theme_dark_secondary = Color(0xFF9E9E9E) // Medium light grey
private val md_theme_dark_onSecondary = Color(0xFF3A3A3A)
private val md_theme_dark_secondaryContainer = Color(0xFF6B6B6B) // Dark grey secondary
private val md_theme_dark_onSecondaryContainer = Color(0xFFFAFAF8)

private val md_theme_dark_background = Color(0xFF1A1A1A) // Warm dark
private val md_theme_dark_onBackground = Color(0xFFE8E6E3)
private val md_theme_dark_surface = Color(0xFF242424) // Warm dark surface
private val md_theme_dark_onSurface = Color(0xFFE8E6E3)

private val md_theme_dark_surfaceVariant = Color(0xFF363636) // Medium warm dark
private val md_theme_dark_onSurfaceVariant = Color(0xFFD0CFC8)
private val md_theme_dark_outline = Color(0xFF525252)

private val md_theme_dark_error = Color(0xFFEF5350)
private val md_theme_dark_onError = Color(0xFF330011)
private val md_theme_dark_errorContainer = Color(0xFFB71C1C)
private val md_theme_dark_onErrorContainer = Color(0xFFFFEBEE)


val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,

    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,

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