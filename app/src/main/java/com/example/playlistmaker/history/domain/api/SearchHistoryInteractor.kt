package com.example.playlistmaker.history.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(track: Track)
    fun clearHistory()

    fun getLastTrack(): Track?

    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }
}