package com.example.playlistmaker.main.di

import com.example.playlistmaker.main.ui.RootActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rootActivityViewModelModule = module {
    viewModel {
        RootActivityViewModel( get())
    }
}
