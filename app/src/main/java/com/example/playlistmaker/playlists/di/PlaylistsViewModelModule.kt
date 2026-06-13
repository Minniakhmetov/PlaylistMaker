package com.example.playlistmaker.playlists.di

import com.example.playlistmaker.playlists.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsViewModelModule = module {
    viewModel {
        PlaylistsViewModel()
    }
}