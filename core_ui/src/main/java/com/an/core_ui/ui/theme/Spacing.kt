package com.an.core_ui.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp,
    val cardSmall: Dp = 96.dp,
    val cardMedium: Dp = 128.dp,
    val cardLarge: Dp = 160.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

data class FontSize(
    val default: TextUnit = 14.sp,
    val extraSmall: TextUnit = 4.sp,
    val small: TextUnit = 8.sp,
    val medium: TextUnit = 12.sp,
    val large: TextUnit = 16.sp,
    val extraLarge: TextUnit = 20.sp,
)

val LocalFontSize = compositionLocalOf { FontSize() }

val MaterialTheme.fontSize: FontSize
    @Composable
    @ReadOnlyComposable
    get() = LocalFontSize.current