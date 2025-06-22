package com.an.facefilters

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.an.facefilters.core.Navigation
import com.an.facefilters.home.data.SettingsDataStore
import com.an.facefilters.home.data.ThemeSettings
import com.an.facefilters.ui.theme.FaceFiltersTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settings: SettingsDataStore by inject()

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERAX_PERMISSIONS,
                0
            )
        }

        //enableEdgeToEdge()
        setContent {
            val theme = settings
                .themeSettings
                .collectAsState(ThemeSettings())
                .value

            FaceFiltersTheme(
                darkTheme = true/*when(theme.darkMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }*/,
                dynamicColor = theme.dynamicColor
            ) {

                val navController = rememberNavController()

                Navigation(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )


            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }


}
