package com.example.playlistmaker.playlists.domain.impl

import android.net.Uri
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
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

    override suspend fun updatePlaylist(
        playlist: Playlist,
        uri: Uri?
    ) {
        playlistsRepository.updatePlaylist(playlist, uri)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun getPlaylist(id: Long): Flow<Playlist?> {
        return playlistsRepository.getPlaylist(id)
    }

    override suspend fun saveTrackInPlaylist(track: Track) {
        playlistsRepository.saveTrackInPlaylist(track)
    }

    override suspend fun deleteTrackInPlaylist(playlist: Playlist, trackId: Long) {
        playlistsRepository.deleteTrackInPlaylist(playlist, trackId)
    }

    override suspend fun deletePlaylist(playlist: Playlist): Boolean {
        return playlistsRepository.deletePlaylist(playlist)
    }

    override fun getTracksInPlaylist(idTracks: String): Flow<List<Track>> {
        return playlistsRepository.getTracksInPlaylist(idTracks)
    }

    override suspend fun addTrackInPlaylist(
        track: Track,
        playlist: Playlist
    ): Boolean {
        return playlistsRepository.addTrackInPlaylist(track, playlist)
    }
}
