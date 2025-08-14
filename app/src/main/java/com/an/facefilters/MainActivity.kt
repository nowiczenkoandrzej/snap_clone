package com.an.facefilters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.an.core_ui.ui.theme.FaceFiltersTheme
import com.an.facefilters.home.data.SettingsDataStore
import com.an.facefilters.home.data.ThemeSettings
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settings: SettingsDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

}
