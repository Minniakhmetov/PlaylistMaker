package com.example.playlistmaker.favoriteTracks.di

import com.example.playlistmaker.favoriteTracks.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.favoriteTracks.data.TrackDbConvertor
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksRepository
import org.koin.dsl.module

val favoriteTracksRepositoryModule = module {
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    factory { TrackDbConvertor() }
}
