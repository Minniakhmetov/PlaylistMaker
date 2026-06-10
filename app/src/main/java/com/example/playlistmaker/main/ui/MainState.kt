package com.example.playlistmaker.main.ui

import com.example.playlistmaker.search.domain.models.Track

sealed interface MainState {
    object Start : MainState

    object OpenSearchActivity : MainState
    object OpenMedicalLibraryActivity : MainState
    object OpenSettingsActivity : MainState
    data class OpenLastTrack(
        val lastActivity: String,
        val track: Track,
    ) : MainState

}
