package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.sharedPrefs.SettingPrefsClient
import com.example.playlistmaker.settings.data.sharedPrefs.SettingsPrefsManager
import org.koin.dsl.module

val settingDataModule = module {
    single<SettingPrefsClient> {
        SettingsPrefsManager(get())
    }
}
