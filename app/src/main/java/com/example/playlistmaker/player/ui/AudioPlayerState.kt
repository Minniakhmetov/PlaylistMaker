package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface AudioPlayerState {
    data class ShowTrack(
        val track: Track,
    ) : AudioPlayerState
}
