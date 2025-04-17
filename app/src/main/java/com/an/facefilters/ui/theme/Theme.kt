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


private val md_theme_light_primary = Color(0xFF006D32)
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_secondary = Color(0xFF4F6353)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_background = Color(0xFFFBFDF7)
private val md_theme_light_onBackground = Color(0xFF191C19)
private val md_theme_light_surface = Color(0xFFFBFDF7)
private val md_theme_light_onSurface = Color(0xFF191C19)

private val md_theme_dark_primary = Color(0xFF5BDB8A)
private val md_theme_dark_onPrimary = Color(0xFF003919)
private val md_theme_dark_secondary = Color(0xFFB4CCB8)
private val md_theme_dark_onSecondary = Color(0xFF1F3524)
private val md_theme_dark_error = Color(0xFFFFB4AB)
private val md_theme_dark_onError = Color(0xFF690005)
private val md_theme_dark_background = Color(0xFF191C19)
private val md_theme_dark_onBackground = Color(0xFFE1E3DD)
private val md_theme_dark_surface = Color(0xFF191C19)
private val md_theme_dark_onSurface = Color(0xFFE1E3DD)



val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface
)

val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface
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