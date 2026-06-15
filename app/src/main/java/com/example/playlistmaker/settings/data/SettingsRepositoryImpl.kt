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

    override fun saveLsatDestinationId(lastDestinationId: Int) {
        settingPrefsClient.putInt(SETTING_LAST_DESTINATION_KEY_ID, lastDestinationId)
    }

    override fun getLsatDestinationId(): Int {
        return settingPrefsClient.getInt(SETTING_LAST_DESTINATION_KEY_ID)
    }

    override fun saveLsatPage(lastPage: Int) {
        settingPrefsClient.putInt(SETTING_LAST_PAGE, lastPage)
    }

    override fun getLsatPage(): Int {
        val id = settingPrefsClient.getInt(SETTING_LAST_PAGE)
        return id
    }

    companion object {
        const val SETTING_LAST_DESTINATION_KEY_ID = "key_setting_last_destination_id"
        const val SETTING_LAST_PAGE = "key_last_page"
        const val SETTING_THEME_KEY = "key_for_setting_theme"
    }

}