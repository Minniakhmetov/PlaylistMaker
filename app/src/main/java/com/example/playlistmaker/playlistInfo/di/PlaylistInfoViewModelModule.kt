package com.example.playlistmaker.playlistInfo.di

import com.example.playlistmaker.playlistInfo.ui.PlaylistInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistInfoViewModelModule = module {
    viewModel {
        PlaylistInfoViewModel(get(), get(), get(), get())
    }
}
