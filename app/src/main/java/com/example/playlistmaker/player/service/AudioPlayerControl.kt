package com.example.playlistmaker.player.service

import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getCurrentPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showNotification()
    fun hideNotification()
}
