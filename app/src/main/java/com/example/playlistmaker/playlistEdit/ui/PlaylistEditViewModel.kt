package com.example.playlistmaker.playlistEdit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlistInfo.ui.PlaylistInfoViewModel.Companion.SAVED_STATE_HANDLE_PLAYLIST_ID
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch


class PlaylistEditViewModel(
    override val playlistsInteractor: PlaylistsInteractor,
    savedStateHandle: SavedStateHandle,
) : PlaylistCreateViewModel(playlistsInteractor) {
    override val stateLiveData = MutableLiveData<PlaylistCreateState>()
    override fun observeState(): LiveData<PlaylistCreateState> = stateLiveData

    private val playlistId: Long? = savedStateHandle[SAVED_STATE_HANDLE_PLAYLIST_ID]
    override var currentPlaylist: Playlist? = null

    override fun init() {
        renderState(PlaylistCreateState.Loading)
        viewModelScope.launch {
            playlistId?.let {
                playlistsInteractor.getPlaylist(it).collect { playlist ->
                    playlist?.let { it1 ->
                        currentPlaylist = it1
                        if (it1.pathImageCover != null) {
                            val uri = playlistsInteractor.getUri(it1.pathImageCover)
                            renderState(PlaylistCreateState.Content(it1, uri))
                        } else {
                            renderState(PlaylistCreateState.Content(it1, null))
                        }
                    }
                }
            }
        }
    }

    override fun savePlaylist() {
        val playlist = currentPlaylist
        if (playlist != null) {
            viewModelScope.launch {
                currentPlaylist = playlist.copy(created = System.currentTimeMillis())
                playlistsInteractor.updatePlaylist(playlist, uri)
            }
        }

    }


    override fun onClickBack() {
        renderState(PlaylistCreateState.Close)
    }

    private fun renderState(state: PlaylistCreateState) {
        stateLiveData.postValue(state)
    }
}
