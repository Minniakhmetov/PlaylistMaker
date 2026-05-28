package com.example.playlistmaker.player.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.main.ui.App
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val track: Track,
    context: Context,
) : ViewModel() {
    companion object {
        const val ACTIVITY_AUDIO_PLAYER_KEY = "key_for_audio_player_activity"
        const val TRACK_TIME_START_VALUE = "00:00"
        private const val TRACK_TIME_DELAY = 400L


        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        fun getFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                AudioPlayerViewModel(track, app)
            }
        }
    }

    private val settingsInteractor = Creator.provideSettingsInteractor(context)

    private val playerStateLiveData = MutableLiveData(STATE_DEFAULT)
    fun observePlayerState(): LiveData<Int> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(TRACK_TIME_START_VALUE)
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData.value == STATE_PLAYING) {
            startTimerUpdate()
        }
    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(STATE_PREPARED)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(STATE_PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(STATE_PAUSED)
    }

    private fun startTimerUpdate() {
        progressTimeLiveData.postValue(
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                mediaPlayer.currentPosition
            )
        )
        handler.postDelayed(timerRunnable, TRACK_TIME_DELAY)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        progressTimeLiveData.postValue(TRACK_TIME_START_VALUE)
    }

    fun onPause() {
        pausePlayer()
    }

    fun onStop() {
        settingsInteractor.saveLastActivity(ACTIVITY_AUDIO_PLAYER_KEY)
    }

}
