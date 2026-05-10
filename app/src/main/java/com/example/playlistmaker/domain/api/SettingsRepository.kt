package com.example.playlistmaker.domain.api

interface SettingsRepository {
    fun saveLastActivity(activity: String)
    fun getLastActivity(): String

    fun getDarkThemeState(): Boolean
    fun setDarkMode()
    fun setDarkMode(state: Boolean)
}
