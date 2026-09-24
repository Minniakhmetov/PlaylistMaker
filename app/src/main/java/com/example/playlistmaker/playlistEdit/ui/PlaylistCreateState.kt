package com.example.playlistmaker.playlistEdit.ui

import android.net.Uri
import com.example.playlistmaker.playlistEdit.domain.models.Playlist

sealed interface PlaylistCreateState {
    object Loading : PlaylistCreateState
    object Empty : PlaylistCreateState
    data class Content(
        val playlist: Playlist,
        val uri: Uri?,
    ) : PlaylistCreateState

    object ShowDialog : PlaylistCreateState
    object Close : PlaylistCreateState
}
