package com.example.playlistmaker.playlists.domain.db

import android.net.Uri
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun savePlaylist(playlist: Playlist, uri: Uri?)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun saveTrackInPlaylist(track: Track)

    suspend fun updatePlaylist(track: Track, playlist: Playlist): Boolean
}
