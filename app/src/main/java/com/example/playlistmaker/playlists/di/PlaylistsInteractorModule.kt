package com.example.playlistmaker.playlists.di

import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import com.example.playlistmaker.playlists.domain.impl.PlaylistsInteractorImpl
import org.koin.dsl.module

val playlistsInteractorModule = module {
    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }
}
