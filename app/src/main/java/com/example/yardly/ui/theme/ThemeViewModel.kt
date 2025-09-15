package com.example.yardly.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val themeSettingsRepository = ThemeSettingsRepository(application)

    val theme: StateFlow<Theme> = themeSettingsRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Theme.DARK
        )

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            themeSettingsRepository.setTheme(theme)
        }
    }
}
