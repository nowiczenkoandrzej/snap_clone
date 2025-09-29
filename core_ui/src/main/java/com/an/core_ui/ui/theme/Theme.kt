package com.an.core_ui.ui.theme
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



private val md_theme_light_primary = Color(0xFF00C2CB) // Snappol turquoise
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFE0F8F9) // Jasny turkus pastel
private val md_theme_light_onPrimaryContainer = Color(0xFF00393D)

private val md_theme_light_secondary = Color(0xFF9B5DE5) // Snappol violet
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFF1E5FB) // Jasny pastel fiolet
private val md_theme_light_onSecondaryContainer = Color(0xFF2F004D)

private val md_theme_light_background = Color(0xFFFFFEFC) // Ciepła biel
private val md_theme_light_onBackground = Color(0xFF1F1F1F)

private val md_theme_light_surface = Color(0xFFFFFFFF)
private val md_theme_light_onSurface = Color(0xFF1F1F1F)

private val md_theme_light_surfaceVariant = Color(0xFFF3F1F6) // Lekki pastel mix
private val md_theme_light_onSurfaceVariant = Color(0xFF525252)
private val md_theme_light_outline = Color(0xFFD0CFC8)

private val md_theme_light_error = Color(0xFFB71C1C)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_errorContainer = Color(0xFFFFEBEE)
private val md_theme_light_onErrorContainer = Color(0xFF660011)

private val md_theme_dark_primary = Color(0xFF00C2CB) // Neonowy turkus
private val md_theme_dark_onPrimary = Color(0xFF000000)
private val md_theme_dark_primaryContainer = Color(0xFF00393D) // Ciemny turkus
private val md_theme_dark_onPrimaryContainer = Color(0xFF9EECF0)

private val md_theme_dark_secondary = Color(0xFF9B5DE5) // Neonowy fiolet
private val md_theme_dark_onSecondary = Color(0xFF000000)
private val md_theme_dark_secondaryContainer = Color(0xFF2F004D)
private val md_theme_dark_onSecondaryContainer = Color(0xFFE5C7F9)

private val md_theme_dark_background = Color(0xFF000000) // Głęboka czerń
private val md_theme_dark_onBackground = Color(0xFFE0E0E0)

private val md_theme_dark_surface = Color(0xFF121212) // Minimalny kontrast
private val md_theme_dark_onSurface = Color(0xFFE0E0E0)

private val md_theme_dark_surfaceVariant = Color(0xFF1E1E1E)
private val md_theme_dark_onSurfaceVariant = Color(0xFFB0B0B0)

private val md_theme_dark_outline = Color(0xFF555555)
private val md_theme_dark_error = Color(0xFFFF4C4C) // Neon red
private val md_theme_dark_onError = Color(0xFF000000)
private val md_theme_dark_errorContainer = Color(0xFF330000)
private val md_theme_dark_onErrorContainer = Color(0xFFFFCCCC)



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
    dynamicColor: Boolean = false,
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