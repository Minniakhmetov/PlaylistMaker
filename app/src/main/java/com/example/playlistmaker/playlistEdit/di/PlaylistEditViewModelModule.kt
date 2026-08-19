package com.example.playlistmaker.playlistEdit.di

import com.example.playlistmaker.playlistEdit.ui.PlaylistEditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlistEditViewModelModule = module {
    viewModel {
        PlaylistEditViewModel(get(), get())
    }
}