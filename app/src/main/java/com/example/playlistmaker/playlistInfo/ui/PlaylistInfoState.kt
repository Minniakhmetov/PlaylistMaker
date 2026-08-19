package com.example.playlistmaker.playlistInfo.ui

import com.example.playlistmaker.playlistEdit.domain.models.Playlist

sealed interface PlaylistInfoState {
    data class Content(
        val playlist: Playlist,
    ) : PlaylistInfoState

    object Close : PlaylistInfoState
}
