package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val settingRepositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}
