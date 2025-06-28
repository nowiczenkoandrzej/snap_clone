package com.an.facefilters.canvas.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun rememberCategoriesList(): List<String> {
    return remember { listOf(
        "Activities",
        "Animals",
        "Clothing",
        "Emojis",
        "Food",
        "Music",
        "Objects"
    ) }
}