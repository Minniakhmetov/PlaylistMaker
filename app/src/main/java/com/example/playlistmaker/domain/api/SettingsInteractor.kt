package com.example.playlistmaker.domain.api


interface SettingsInteractor {
    fun saveLastActivity(activityKey: String)
    fun getLastActivity(): String

    fun getDarkThemeState(): Boolean
    fun setDarkMode()
    fun setDarkMode(state: Boolean)
}
