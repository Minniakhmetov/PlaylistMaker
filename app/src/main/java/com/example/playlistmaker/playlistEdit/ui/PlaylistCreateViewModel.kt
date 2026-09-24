package com.example.playlistmaker.playlistEdit.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch


open class PlaylistCreateViewModel(
    open val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    open val stateLiveData = MutableLiveData<PlaylistCreateState>()
    open fun observeState(): LiveData<PlaylistCreateState> = stateLiveData
    private val showMessageLiveData = SingleLiveEvent<String>()
    open fun observeShowMessage(): LiveData<String> = showMessageLiveData

    open var uri: Uri? = null

    open fun init() {
        renderState(PlaylistCreateState.Loading)
    }

    open var currentPlaylist: Playlist? = null
    open fun updateName(newName: String) {
        val playlist = currentPlaylist
        currentPlaylist = playlist?.copy(name = newName)
            ?: Playlist(
                0,
                newName,
                null,
                null,
                null,
                null,
                0
            )
    }

    open fun updateDescription(newDescription: String) {
        val playlist = currentPlaylist
        currentPlaylist = playlist?.copy(description = newDescription)
            ?: Playlist(
                0,
                "",
                newDescription,
                null,
                null,
                null,
                0
            )
    }

    open fun updateUri(newUri: Uri) {
        uri = newUri
    }

    open fun savePlaylist() {
        val playlist = currentPlaylist
        if (playlist != null) {
            currentPlaylist = playlist.copy(created = System.currentTimeMillis())

            viewModelScope.launch {
                playlistsInteractor.savePlaylist(playlist, uri)
            }

            showMessageLiveData.postValue(playlist.name)
        }
    }

    open fun onClickBack() {
        val playlist = currentPlaylist
        if (playlist?.name?.isEmpty() == true && playlist.description.isNullOrEmpty() && uri == null) {
            renderState(PlaylistCreateState.Close)
        } else {
            renderState(PlaylistCreateState.ShowDialog)
        }
    }

    private fun renderState(state: PlaylistCreateState) {
        stateLiveData.postValue(state)
    }
}
