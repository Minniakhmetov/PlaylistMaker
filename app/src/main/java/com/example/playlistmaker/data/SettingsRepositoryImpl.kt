package com.example.playlistmaker.data

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SettingsRepository


class SettingsRepositoryImpl(private val settingsManager: SettingsManager) : SettingsRepository {

    override fun saveLastActivity(activity: String) {
        settingsManager.putString(SETTING_ACTIVITY_LAST, activity)
    }

    override fun getLastActivity(): String {
        return settingsManager.getString(SETTING_ACTIVITY_LAST)
    }

    override fun getDarkThemeState(): Boolean {
        return settingsManager.getBoolean(SETTING_THEME_KEY)
    }

    override fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
            if (getDarkThemeState()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun setDarkMode(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsManager.putBoolean(SETTING_THEME_KEY, state)
    }

    companion object {
        const val SETTING_ACTIVITY_LAST = "setting_activity_last"
        const val SETTING_THEME_KEY = "key_for_setting_theme"
    }
}
