package com.example.yardly.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class Theme { // Renamed from ThemeSettings to avoid conflict
    LIGHT,
    DARK,
    SYSTEM
}

data class ThemeSettings(val theme: Theme)

class ThemeSettingsRepository(context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
    }

    val theme: Flow<Theme> = dataStore.data.map {
        Theme.valueOf(it[PreferencesKeys.THEME] ?: Theme.DARK.name)
    }

    suspend fun setTheme(theme: Theme) {
        dataStore.edit {
            it[PreferencesKeys.THEME] = theme.name
        }
    }
}
