package com.example.playlistmaker.main.ui

sealed interface RootActivityState {

    data class OpenLastPage(
        val lastDestinationId: Int,
    ) : RootActivityState

}
