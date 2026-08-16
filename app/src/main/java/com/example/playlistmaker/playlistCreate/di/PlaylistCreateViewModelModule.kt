package com.example.playlistmaker.playlistCreate.di

import com.example.playlistmaker.playlistCreate.ui.PlaylistCreateViewModel
import com.example.playlistmaker.playlists.ui.PlaylistsViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlistCreateViewModelModule = module {
    viewModel {
        PlaylistCreateViewModel(get())
    }
}