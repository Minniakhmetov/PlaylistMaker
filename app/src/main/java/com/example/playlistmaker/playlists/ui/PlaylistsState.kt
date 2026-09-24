package com.example.playlistmaker.playlists.ui

import com.example.playlistmaker.playlistEdit.domain.models.Playlist

sealed interface PlaylistsState {
    object Empty : PlaylistsState

    data class Content(
        val playlists: List<Playlist>,
    ) : PlaylistsState

}
