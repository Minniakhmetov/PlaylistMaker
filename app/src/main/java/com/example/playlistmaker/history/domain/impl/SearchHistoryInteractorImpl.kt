package com.example.playlistmaker.history.domain.impl

import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.history.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository,
) : SearchHistoryInteractor {
    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getHistory().data)
    }

    override fun saveToHistory(track: Track) {
        repository.saveToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getLastTrack(): Track? {
        return repository.getLastTrack()
    }
}
