package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}
