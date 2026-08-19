package com.example.playlistmaker.playlistEdit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import com.example.playlistmaker.playlistInfo.ui.PlaylistInfoViewModel.Companion.SAVED_STATE_HANDLE_PLAYLIST_ID


class PlaylistEditViewModel(
    override val playlistsInteractor: PlaylistsInteractor,
    private val savedStateHandle: SavedStateHandle,
) : PlaylistCreateViewModel(playlistsInteractor) {
    override val stateLiveData = MutableLiveData<PlaylistCreateState>()
    override fun observeState(): LiveData<PlaylistCreateState> = stateLiveData

    private val playlistId: Long? = savedStateHandle[SAVED_STATE_HANDLE_PLAYLIST_ID]
    override var currentPlaylist: Playlist = Playlist(
        0,
        "",
        null,
        null,
        null,
        null,
        0
    )

    override fun init() {
        renderState(PlaylistCreateState.Loading)
        viewModelScope.launch {
            playlistId?.let {
                playlistsInteractor.getPlaylist(it).collect { playlist ->
                    playlist?.let { it1 ->
                        currentPlaylist = it1
                        renderState(PlaylistCreateState.Content(it1))
                    }
                }
            }
        }
    }

    override fun savePlaylist(){
        viewModelScope.launch {
            currentPlaylist = currentPlaylist.copy(created = System.currentTimeMillis())
            playlistsInteractor.updatePlaylist(currentPlaylist, uri)
        }
    }

    override fun onClickBack(){
        renderState(PlaylistCreateState.Close)
    }

    private fun renderState(state: PlaylistCreateState) {
        stateLiveData.postValue(state)
    }
}
