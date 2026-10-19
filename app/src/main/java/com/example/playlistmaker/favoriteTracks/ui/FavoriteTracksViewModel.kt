package com.example.playlistmaker.favoriteTracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoriteTracksState>(FavoriteTracksState.Empty)
    val uiState: StateFlow<FavoriteTracksState> = _uiState.asStateFlow()

    init {
        searchFavoriteTracks()
    }

    fun searchFavoriteTracks() {
        viewModelScope.launch {
            favoriteTracksInteractor.getFavoriteTracksFlow().collect { favoriteTracks ->
                if (favoriteTracks.isEmpty()) {
                    renderState(FavoriteTracksState.Empty)
                } else {
                    renderState(FavoriteTracksState.Content(favoriteTracks))
                }
            }
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        _uiState.value = state
    }
}
