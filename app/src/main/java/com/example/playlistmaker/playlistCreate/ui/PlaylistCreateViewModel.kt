package com.example.playlistmaker.playlistCreate.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch
import android.net.Uri
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent


class PlaylistCreateViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistCreateState>()
    fun observeState(): LiveData<PlaylistCreateState> = stateLiveData
    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    var newPlaylist: Playlist = Playlist(
        0,
        "",
        null,
        null,
        null,
        null,
        0
    )

    var uri: Uri? = null

    fun updateName(newName: String){
        newPlaylist = newPlaylist.copy(name = newName)
    }

    fun updateDescription(newDescription: String){
        newPlaylist = newPlaylist.copy(description = newDescription)
    }

    fun updateUri(newUri: Uri){
        uri = newUri
    }

    fun createPlaylist(){
        newPlaylist = newPlaylist.copy(created = System.currentTimeMillis())
        viewModelScope.launch {
            playlistsInteractor.savePlaylist(newPlaylist, uri)
        }
        showMessageLiveData.postValue("Плейлист ${newPlaylist.name} создан")
    }

    fun onClickBack(){
        if (newPlaylist.name.isEmpty() && newPlaylist.description.isNullOrEmpty() && uri == null){
            renderState(PlaylistCreateState.Close)
        }else{
            renderState(PlaylistCreateState.ShowDialog)
        }
    }

    private fun renderState(state: PlaylistCreateState) {
        stateLiveData.postValue(state)
    }
}
