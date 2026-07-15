package com.example.playlistmaker.settings.data


import com.example.playlistmaker.settings.data.sharedPrefs.SettingPrefsClient
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.util.ThemeSettings

class SettingsRepositoryImpl(
    private val settingPrefsClient: SettingPrefsClient,
) :
    SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(settingPrefsClient.getBoolean(SETTING_THEME_KEY))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingPrefsClient.putBoolean(SETTING_THEME_KEY, settings.darkTheme)
    }

    companion object {
        const val SETTING_THEME_KEY = "key_for_setting_theme"
    }
}
