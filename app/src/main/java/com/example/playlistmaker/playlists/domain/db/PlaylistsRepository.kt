package com.example.playlistmaker.playlists.domain.db

import android.net.Uri
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun savePlaylist(playlist: Playlist, uri: Uri?)
    suspend fun updatePlaylist(playlist: Playlist, uri: Uri?)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(id: Long): Flow<Playlist?>

    suspend fun deletePlaylist(playlist: Playlist): Boolean

    suspend fun saveTrackInPlaylist(track: Track)

    suspend fun deleteTrackInPlaylist(playlist: Playlist, trackId: Long)

    fun getTracksInPlaylist(idTracks: String): Flow<List<Track>>

    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean
}
