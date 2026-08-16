package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.favoriteTracks.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }
}