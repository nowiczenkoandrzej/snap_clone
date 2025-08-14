package com.an.facefilters

sealed class Screen(val route: String) {
    object Canvas: Screen(route = "Canvas")
    object Home: Screen(route = "Home")
    object Settings: Screen(route = "Settings")
    object Drawing: Screen(route = "Drawing")
    object Stickers: Screen(route = "Stickers")
    object CreateSticker: Screen(route = "Create Sticker")
    object Filters: Screen(route = "Filters")
    object Crop: Screen(route = "Crop")
    object EditText: Screen(route = "Edit Text")
    object EditImage: Screen(route = "Edit Image")
    object AddText: Screen(route = "Add Text")
    object Rubber: Screen(route = "Rubber")
}