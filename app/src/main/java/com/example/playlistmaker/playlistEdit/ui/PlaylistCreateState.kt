package com.example.playlistmaker.playlistEdit.ui

import com.example.playlistmaker.playlistEdit.domain.models.Playlist

sealed interface PlaylistCreateState {
    object Loading : PlaylistCreateState
    object Empty : PlaylistCreateState
    data class Content(
        val playlist: Playlist,
    ) : PlaylistCreateState
    object ShowDialog : PlaylistCreateState
    object Close : PlaylistCreateState
}
