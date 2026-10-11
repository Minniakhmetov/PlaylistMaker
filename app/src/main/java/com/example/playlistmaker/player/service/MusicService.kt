package com.example.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.AudioPlayerFragment.Companion.TRACK_ARTIST_KEY
import com.example.playlistmaker.player.ui.AudioPlayerFragment.Companion.TRACK_NAME_KEY
import com.example.playlistmaker.player.ui.AudioPlayerFragment.Companion.TRACK_URL_KEY
import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {
    private var mediaPlayer: MediaPlayer? = null
    private var trackUrl = ""
    private var trackName = ""
    private var trackArtist = ""
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerState = _playerState.asStateFlow()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer()
    }

    private val binder = MusicServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        trackUrl = intent?.getStringExtra(TRACK_URL_KEY) ?: ""
        trackName = intent?.getStringExtra(TRACK_NAME_KEY) ?: ""
        trackArtist = intent?.getStringExtra(TRACK_ARTIST_KEY) ?: ""
        preparePlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        releasePlayer()
    }

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(TRACK_TIME_DELAY)
                _playerState.value =
                    PlayerState.Playing(dateFormat.format(mediaPlayer?.currentPosition))
            }
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun preparePlayer() {
        if (trackUrl.isEmpty()) return
        mediaPlayer?.setDataSource(trackUrl)
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = PlayerState.Prepared()
            resetTimer()
            hideNotification()
        }
        mediaPlayer?.prepareAsync()
    }

    private fun resetTimer() {
        timerJob?.cancel()
    }

    override fun getCurrentPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(dateFormat.format(mediaPlayer?.currentPosition))
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(dateFormat.format(mediaPlayer?.currentPosition))
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(
            this,
            ServiceCompat.STOP_FOREGROUND_REMOVE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(getString(R.string.app_name))
            .setContentText("$trackArtist - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private companion object {
        private const val TRACK_TIME_DELAY = 400L
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Music service"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Service for playing music"
        const val SERVICE_NOTIFICATION_ID = 100
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}
