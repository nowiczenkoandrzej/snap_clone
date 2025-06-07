package com.an.facefilters.core

sealed class Screen(val route: String) {
    object Camera: Screen(route = "Camera")
    object Canvas: Screen(route = "Canvas")
    object Home: Screen(route = "Home")
    object Settings: Screen(route = "Settings")
    object CropImage: Screen(route = "Crop Image")
    object CreateSticker: Screen(route = "Create Sticker")
}