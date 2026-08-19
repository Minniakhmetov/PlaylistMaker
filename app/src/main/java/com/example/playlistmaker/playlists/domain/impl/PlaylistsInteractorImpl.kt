package com.example.playlistmaker.playlists.domain.impl

import android.net.Uri
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import com.example.playlistmaker.playlists.domain.db.PlaylistsRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist, uri: Uri?) {
        playlistsRepository.savePlaylist(playlist, uri)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun saveTrackInPlaylist(track: Track) {
        playlistsRepository.saveTrackInPlaylist(track)
    }

    override suspend fun updatePlaylist(
        track: Track,
        playlist: Playlist
    ): Boolean {
        return playlistsRepository.updatePlaylist(track, playlist)
    }
}
