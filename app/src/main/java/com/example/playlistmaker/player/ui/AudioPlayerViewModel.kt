package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val gson: Gson,
    private val messageTrackAlreadyAddedPlaylist: String,
    private val messageTrackAddedPlaylist: String,
) : ViewModel() {
    private val playerStateLiveData = MutableLiveData(PlayerState.DEFAULT)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(TRACK_TIME_START_VALUE)
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val isShowBottomSheet = MutableLiveData(false)
    fun observeShowBottomSheet(): LiveData<Boolean> = isShowBottomSheet

    private val mediaPlayer = MediaPlayer()

    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData
    private val trackIsFavoriteLiveData = MutableLiveData(false)
    fun observeTrackIsFavorite(): LiveData<Boolean> = trackIsFavoriteLiveData
    private val playlistsCurrent = MutableLiveData<List<Playlist>>()
    fun observePlaylistsCurrent(): LiveData<List<Playlist>> = playlistsCurrent
    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    private val track: Track? = savedStateHandle[SAVED_STATE_HANDLE_TRACK]

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            PlayerState.DEFAULT -> {}
            PlayerState.PREPARED, PlayerState.PAUSED -> startPlayer()
            PlayerState.PLAYING -> pausePlayer()
            else -> {}
        }
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

    fun onClickPlaylist(playlist: Playlist){
        track?.let {
            if(playlistContainsTrack(it, playlist)){
                showMessageLiveData.postValue("$messageTrackAlreadyAddedPlaylist ${playlist.name}")
            }else{
                viewModelScope.launch {
                    val result = playlistsInteractor.addTrackInPlaylist(track, playlist)
                    if (result){
                        renderState(AudioPlayerState.ShowTrack(track))
                        showMessageLiveData.postValue("$messageTrackAddedPlaylist ${playlist.name}")
                    }
                }
            }
        }
    }

    fun onClickCreateNewPlaylist(){
        isShowBottomSheet.postValue(false)
    }

    fun playlistContainsTrack(track: Track, playlist: Playlist): Boolean{
        if (playlist.trackIds == null){
            return false
        }else{
            val type = object : TypeToken<List<Long>>() {}.type
            val trackIds: List<Long> = gson.fromJson(playlist.trackIds, type)
            return trackIds.contains(track.trackId)
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.PREPARED)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(PlayerState.PAUSED)
    }

    private fun startTimerUpdate() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TRACK_TIME_DELAY)
                progressTimeLiveData.postValue(
                    dateFormat.format(mediaPlayer.currentPosition)
                )
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        progressTimeLiveData.postValue(TRACK_TIME_START_VALUE)
    }

    fun onPause() {
        pausePlayer()
    }

    fun initTrack() {
        track?.let {
            viewModelScope.launch {
                favoriteTracksInteractor.getFavoriteTracksFlow().collect { favoriteTracks ->
                    trackIsFavoriteLiveData.value = favoriteTracks.any{it.trackId == track.trackId }
                }
            }

            renderState(
                AudioPlayerState.ShowTrack(
                    track = track
                )
            )
            preparePlayer(track)
        }
    }

    fun trackAddPlaylistClicked(){
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
        const val TRACK_TIME_START_VALUE = "00:00"
        private const val TRACK_TIME_DELAY = 400L
        const val SAVED_STATE_HANDLE_TRACK = "track"
    }
}
