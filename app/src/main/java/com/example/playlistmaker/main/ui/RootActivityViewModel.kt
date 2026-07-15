package com.example.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class RootActivityViewModel(
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    fun installTheme() {
        settingsInteractor.installTheme()
    }
}
