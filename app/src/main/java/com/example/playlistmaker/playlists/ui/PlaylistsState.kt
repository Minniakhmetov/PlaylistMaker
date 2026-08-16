package com.example.playlistmaker.playlists.ui

import com.example.playlistmaker.playlistCreate.domain.models.Playlist

sealed interface PlaylistsState {
    object Empty : PlaylistsState

    data class Content(
        val playlists: List<Playlist>,
    ) : PlaylistsState

}
