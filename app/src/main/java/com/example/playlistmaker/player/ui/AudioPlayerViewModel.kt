package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val historyInteractor: SearchHistoryInteractor
) : ViewModel() {
    private val playerStateLiveData = MutableLiveData(PlayerState.DEFAULT)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(TRACK_TIME_START_VALUE)
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val mediaPlayer = MediaPlayer()

    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData

    init {
        loadTrack()
    }

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
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TRACK_TIME_DELAY)
                progressTimeLiveData.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                        mediaPlayer.currentPosition
                    )
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

    fun loadTrack() {
        val trackNew = historyInteractor.getLastTrack()
        trackNew?.let {
            renderState(
                AudioPlayerState.ShowTrack(
                    track = it
                )
            )
            preparePlayer(trackNew)
        }
    }

    private fun renderState(state: AudioPlayerState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val TRACK_TIME_START_VALUE = "00:00"
        private const val TRACK_TIME_DELAY = 400L
    }

}
