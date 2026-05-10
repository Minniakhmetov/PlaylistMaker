package com.example.playlistmaker.presentation.ui

import android.app.Application
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SettingsInteractor


class App : Application() {
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.provideSettingsInteractor(this)
        settingsInteractor.setDarkMode()
    }
}
