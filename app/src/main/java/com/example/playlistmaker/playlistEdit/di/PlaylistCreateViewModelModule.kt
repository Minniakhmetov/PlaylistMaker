package com.example.playlistmaker.playlistEdit.di

import com.example.playlistmaker.playlistEdit.ui.PlaylistCreateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlistCreateViewModelModule = module {
    viewModel {
        PlaylistCreateViewModel(get())
    }
}