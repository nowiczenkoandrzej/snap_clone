package com.an.facefilters

import com.an.feature_drawing.presentation.util.DrawingModeArg

sealed class Screen(val route: String) {
    object Canvas: Screen(route = "Canvas")
    object Home: Screen(route = "Home")
    object Settings: Screen(route = "Settings")
    object Stickers: Screen(route = "Stickers")
    object Filters: Screen(route = "Filters")
    object Crop: Screen(route = "Crop")
    object EditText: Screen(route = "Edit Text")
    object EditImage: Screen(route = "Edit Image")
    object AddText: Screen(route = "Add Text")
    object Drawing: Screen(route = "Drawing/${DrawingModeArg.PENCIL}")
    object Cutting: Screen(route = "Drawing/${DrawingModeArg.CUT}")
    object Rubber: Screen(route = "Drawing/${DrawingModeArg.ERASER}")
}