package com.example.playlistmaker.favoriteTracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    init {
        searchFavoriteTracks()
    }

    fun searchFavoriteTracks() {
        renderState(FavoriteTracksState.Empty)
    }

    private fun renderState(state: FavoriteTracksState) {
        stateLiveData.postValue(state)
    }

}