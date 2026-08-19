package com.example.playlistmaker.playlists.di

import com.example.playlistmaker.player.data.TrackInPlaylistDbConvertor
import com.example.playlistmaker.playlistEdit.data.PlaylistDbConvertor
import com.example.playlistmaker.playlists.data.db.PlaylistsRepositoryImpl
import com.example.playlistmaker.playlists.domain.db.PlaylistsRepository
import org.koin.dsl.module

val playlistsRepositoryModule = module {
    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get(), get(), get())
    }

    factory { PlaylistDbConvertor() }
    factory { TrackInPlaylistDbConvertor() }
}
