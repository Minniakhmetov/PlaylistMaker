package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent
import com.example.playlistmaker.player.service.AudioPlayerControl
import com.example.playlistmaker.player.ui.PlayerState.Default
import com.example.playlistmaker.player.ui.PlayerState.Paused
import com.example.playlistmaker.player.ui.PlayerState.Playing
import com.example.playlistmaker.player.ui.PlayerState.Prepared
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    savedStateHandle: SavedStateHandle,
    private val gson: Gson,
    private val messageTrackAlreadyAddedPlaylist: String,
    private val messageTrackAddedPlaylist: String,
) : ViewModel() {
    private val playerState = MutableLiveData<PlayerState>(Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private var audioPlayerControl: AudioPlayerControl? = null
    private val isShowBottomSheet = MutableLiveData(false)
    fun observeShowBottomSheet(): LiveData<Boolean> = isShowBottomSheet
    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData
    private val trackIsFavoriteLiveData = MutableLiveData(false)
    fun observeTrackIsFavorite(): LiveData<Boolean> = trackIsFavoriteLiveData
    private val playlistsCurrent = MutableLiveData<List<Playlist>>()
    fun observePlaylistsCurrent(): LiveData<List<Playlist>> = playlistsCurrent
    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    private val track: Track? = savedStateHandle[SAVED_STATE_HANDLE_TRACK]

    fun getCurrentTrack(): Track? {
        return track
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getCurrentPlayerState().collect {
                playerState.postValue(it)
            }
        }
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is Default, null -> {}
            is Prepared, is Paused -> audioPlayerControl?.startPlayer()
            is Playing -> audioPlayerControl?.pausePlayer()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun onFavoriteClicked() {
        if (trackIsFavoriteLiveData.value == true) {
            viewModelScope.launch {
                track?.let { favoriteTracksInteractor.deleteFavoriteTrack(it) }
                trackIsFavoriteLiveData.value = false
            }
        } else {
            viewModelScope.launch {
                track?.let { favoriteTracksInteractor.saveFavoriteTrack(it) }
                trackIsFavoriteLiveData.value = true
            }
        }
    }

    fun onClickPlaylist(playlist: Playlist) {
        track?.let {
            if (playlistContainsTrack(it, playlist)) {
                showMessageLiveData.postValue("$messageTrackAlreadyAddedPlaylist ${playlist.name}")
            } else {
                viewModelScope.launch {
                    val result = playlistsInteractor.addTrackInPlaylist(track, playlist)
                    if (result) {
                        renderState(AudioPlayerState.ShowTrack(track))
                        showMessageLiveData.postValue("$messageTrackAddedPlaylist ${playlist.name}")
                    }
                }
            }
        }
    }

    fun onClickCreateNewPlaylist() {
        isShowBottomSheet.postValue(false)
    }

    fun playlistContainsTrack(track: Track, playlist: Playlist): Boolean {
        if (playlist.trackIds == null) {
            return false
        } else {
            val type = object : TypeToken<List<Long>>() {}.type
            val trackIds: List<Long> = gson.fromJson(playlist.trackIds, type)
            return trackIds.contains(track.trackId)
        }
    }

    fun onResume() {
        if (playerState.value is Playing) {
            audioPlayerControl?.hideNotification()
        }
    }

    fun onPause() {
        if (playerState.value is Playing) {
            audioPlayerControl?.showNotification()
        }
    }

    fun initTrack() {
        track?.let {
            viewModelScope.launch {
                favoriteTracksInteractor.getFavoriteTracksFlow().collect { favoriteTracks ->
                    trackIsFavoriteLiveData.value =
                        favoriteTracks.any { it.trackId == track.trackId }
                }
            }

            renderState(
                AudioPlayerState.ShowTrack(
                    track = track
                )
            )
        }
    }

    fun trackAddPlaylistClicked() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect { playlists ->
                playlistsCurrent.value = playlists
            }
        }
        isShowBottomSheet.postValue(true)
    }

    private fun renderState(state: AudioPlayerState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SAVED_STATE_HANDLE_TRACK = "track"
    }
}
