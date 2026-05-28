package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.util.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
    fun installTheme()

    fun saveLastActivity(activity: String)
    fun getLastActivity(): String
}
