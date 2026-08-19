package com.example.playlistmaker.playlistEdit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import kotlinx.coroutines.launch
import android.net.Uri
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor


open class PlaylistCreateViewModel(
    open val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    open val stateLiveData = MutableLiveData<PlaylistCreateState>()
    open fun observeState(): LiveData<PlaylistCreateState> = stateLiveData
    private val showMessageLiveData = SingleLiveEvent<String>()
    open fun observeShowMessage(): LiveData<String> = showMessageLiveData

    open var currentPlaylist: Playlist = Playlist(
        0,
        "",
        null,
        null,
        null,
        null,
        0
    )

    open var uri: Uri? = null

    open fun init(){
        renderState(PlaylistCreateState.Loading)
    }
    open fun updateName(newName: String){
        currentPlaylist = currentPlaylist.copy(name = newName)
    }

    open fun updateDescription(newDescription: String){
        currentPlaylist = currentPlaylist.copy(description = newDescription)
    }

    open fun updateUri(newUri: Uri){
        uri = newUri
    }

    open fun savePlaylist(){
        currentPlaylist = currentPlaylist.copy(created = System.currentTimeMillis())
        viewModelScope.launch {
            playlistsInteractor.savePlaylist(currentPlaylist, uri)
        }
        showMessageLiveData.postValue("Плейлист ${currentPlaylist.name} создан")
    }

    open fun onClickBack(){
        if (currentPlaylist.name.isEmpty() && currentPlaylist.description.isNullOrEmpty() && uri == null){
            renderState(PlaylistCreateState.Close)
        }else{
            renderState(PlaylistCreateState.ShowDialog)
        }
    }

    private fun renderState(state: PlaylistCreateState) {
        stateLiveData.postValue(state)
    }
}
