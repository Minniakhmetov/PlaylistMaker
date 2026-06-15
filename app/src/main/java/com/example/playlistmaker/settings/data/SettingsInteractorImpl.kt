package com.example.playlistmaker.settings.data

import android.util.Log
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

    override fun saveLastDestinationId(lastDestinationId: Int) {
        repository.saveLsatDestinationId(lastDestinationId)
    }

    override fun getLastDestinationId(): Int {
        return repository.getLsatDestinationId()
    }

    override fun saveLastPage(lastPage: Int) {
        repository.saveLsatPage(lastPage)
    }

    override fun getLastPage(): Int {
        return repository.getLsatPage()
    }
}
