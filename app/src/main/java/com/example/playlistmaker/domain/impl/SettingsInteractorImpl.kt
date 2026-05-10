package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository


class SettingsInteractorImpl(
    private val repository: SettingsRepository,
) : SettingsInteractor {

    override fun saveLastActivity(activityKey: String) {
        repository.saveLastActivity(activityKey)
    }

    override fun getLastActivity(): String {
        return repository.getLastActivity()
    }

    override fun getDarkThemeState(): Boolean {
        return repository.getDarkThemeState()
    }

    override fun setDarkMode() {
        repository.setDarkMode()
    }

    override fun setDarkMode(state: Boolean) {
        repository.setDarkMode(state)
    }
}