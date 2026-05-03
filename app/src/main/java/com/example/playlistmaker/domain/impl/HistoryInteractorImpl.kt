package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor {
    override fun getLastTrack(): Track {
        return repository.getLastTrack()
    }

    override fun getTrack(id: Int): Track {
        return repository.getTrack(id)
    }

    override fun getTracks(): List<Track> {
        return repository.getTracks()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }
}
