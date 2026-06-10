package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.dsl.module

val settingInteractorModule = module {
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}
