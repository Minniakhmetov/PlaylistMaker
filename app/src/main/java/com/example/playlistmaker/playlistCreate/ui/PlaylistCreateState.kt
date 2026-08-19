package com.example.playlistmaker.playlistCreate.ui

import com.example.playlistmaker.playlistCreate.domain.models.Playlist

sealed interface PlaylistCreateState {
    object Empty : PlaylistCreateState
    data class Content(
        val playlist: Playlist,
    ) : PlaylistCreateState
    object ShowDialog : PlaylistCreateState
    object Close : PlaylistCreateState
}
