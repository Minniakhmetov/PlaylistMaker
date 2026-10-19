package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.util.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val _themeSettings = MutableStateFlow(settingsInteractor.getThemeSettings())
    val themeSettings: StateFlow<ThemeSettings> = _themeSettings.asStateFlow()

    fun updateThemeSetting(state: Boolean) {
        val newThemeSetting = ThemeSettings(state)
        settingsInteractor.updateThemeSetting(newThemeSetting)
        _themeSettings.value = newThemeSetting
    }

    fun openLink() {
        sharingInteractor.openTerms()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}
