package com.example.playlistmaker.search.di

import com.example.playlistmaker.favoriteTracks.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.favoriteTracks.data.TrackDbConvertor
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory { TrackDbConvertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
}
