package com.example.playlistmaker.favoriteTracks.di

import androidx.room.Room
import com.example.playlistmaker.favoriteTracks.ui.FavoriteTracksViewModel
import com.example.playlistmaker.main.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksViewModelModule = module {
    viewModel {
        FavoriteTracksViewModel(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
