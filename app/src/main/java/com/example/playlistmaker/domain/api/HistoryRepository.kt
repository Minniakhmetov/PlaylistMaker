package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun getLastTrack(): Track

    fun getTrack(id: Int): Track

    fun getTracks(): List<Track>

    fun clearHistory()

    fun saveTrack(track: Track)
}
