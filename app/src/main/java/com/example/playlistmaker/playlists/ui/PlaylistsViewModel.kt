package com.example.playlistmaker.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        searchPlaylists()
    }

    fun searchPlaylists() {
        renderState(PlaylistsState.Empty)
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }
}