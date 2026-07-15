package com.example.playlistmaker.library.ui

sealed interface LibraryState {
    data class OpenLastPage(
        val lastPageId: Int,
    ) : LibraryState

}
