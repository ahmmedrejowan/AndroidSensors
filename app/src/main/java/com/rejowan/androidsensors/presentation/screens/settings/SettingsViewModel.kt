package com.rejowan.androidsensors.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rejowan.androidsensors.data.preferences.ThemeMode
import com.rejowan.androidsensors.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeMode
    val dynamicColorEnabled: StateFlow<Boolean> = themePreferences.dynamicColorEnabled

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themePreferences.setThemeMode(mode)
        }
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDynamicColorEnabled(enabled)
        }
    }
}
