package com.example.playlistmaker.favoriteTracks.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface FavoriteTracksState {
    data class Content(
        val favoriteTracks: List<Track>,
    ) : FavoriteTracksState

    object Empty : FavoriteTracksState
}