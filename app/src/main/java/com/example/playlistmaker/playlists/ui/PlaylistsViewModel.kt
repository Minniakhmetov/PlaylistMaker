package com.example.playlistmaker.playlists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlaylistsState>(PlaylistsState.Empty)
    val uiState: StateFlow<PlaylistsState> = _uiState.asStateFlow()

    init {
        searchPlaylists()
    }

    fun searchPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    renderState(PlaylistsState.Empty)
                } else {
                    renderState(PlaylistsState.Content(playlists))
                }
            }
        }
    }

    private fun renderState(state: PlaylistsState) {
        _uiState.value = state
    }
}
