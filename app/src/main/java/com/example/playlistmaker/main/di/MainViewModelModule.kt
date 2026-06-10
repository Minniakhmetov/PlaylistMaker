package com.example.playlistmaker.main.di

import com.example.playlistmaker.main.ui.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module {
    viewModel {
        MainActivityViewModel(get(), get())
    }
}
