package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val historyManager: HistoryManager) : HistoryRepository {
    override fun getLastTrack(): Track {
        return historyManager.historyTracks[0]
    }

    override fun getTrack(id: Int): Track {
        return historyManager.historyTracks[id]
    }

    override fun getTracks(): List<Track> {
        return historyManager.historyTracks
    }

    override fun clearHistory() {
        historyManager.clearHistory()
    }

    override fun saveTrack(track: Track) {
        historyManager.saveTrack(track)
    }
}