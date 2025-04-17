package com.an.facefilters.home.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsDataStore(
    private val context: Context
) {

    private val Context.settingsDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "settings")

    object PreferencesKeys {
        val DARK_MODE = stringPreferencesKey("dark_mode")


        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    }

    private val dataStore = context.settingsDataStore



    val themeSettings: Flow<ThemeSettings> = dataStore.data
        .catch { e ->
            emit(emptyPreferences())
        }
        .map { preferences ->
            ThemeSettings(
                darkMode = enumValueOf<ThemeMode>(
                    preferences[PreferencesKeys.DARK_MODE] ?: "SYSTEM"
                ),
                dynamicColor = preferences[PreferencesKeys.DYNAMIC_COLOR] ?: false
            )
        }

    fun updateThemeSettings(
        darkMode: ThemeMode,
        dynamicColor: Boolean
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.DARK_MODE] = darkMode.name
                preferences[PreferencesKeys.DYNAMIC_COLOR] = dynamicColor
            }
        }
    }


}

data class ThemeSettings(
    val darkMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true
)

enum class ThemeMode { LIGHT, DARK, SYSTEM }
