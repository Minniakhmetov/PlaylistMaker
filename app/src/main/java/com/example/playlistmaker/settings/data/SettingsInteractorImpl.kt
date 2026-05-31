package com.example.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.util.ThemeSettings

class SettingsInteractorImpl(
    private val repository: SettingsRepository,
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
        installTheme()
    }

    override fun installTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (getThemeSettings().darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun saveLastActivity(activity: String) {
        repository.saveLastActivity(activity)
    }

    override fun getLastActivity(): String {
        return repository.getLastActivity()
    }
}
