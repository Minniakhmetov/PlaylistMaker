package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.util.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)

    fun saveLastActivity(activity: String)
    fun getLastActivity(): String
}