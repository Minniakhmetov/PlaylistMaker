package com.example.playlistmaker.history.domain.api

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(track: Track)
    suspend fun getHistory(): Resource<List<Track>>
    fun clearHistory()
}
