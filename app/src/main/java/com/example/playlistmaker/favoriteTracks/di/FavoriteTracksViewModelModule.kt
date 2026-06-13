package com.example.playlistmaker.favoriteTracks.di

import com.example.playlistmaker.favoriteTracks.ui.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksViewModelModule = module {
    viewModel {
        FavoriteTracksViewModel()
    }
}