package com.example.playlistmaker.favoriteTracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

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
        stateLiveData.postValue(state)
    }

}