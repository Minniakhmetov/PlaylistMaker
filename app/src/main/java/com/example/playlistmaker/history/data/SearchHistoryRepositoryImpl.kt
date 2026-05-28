package com.example.playlistmaker.history.data


import com.example.playlistmaker.history.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>,
) : SearchHistoryRepository {

    override fun saveToHistory(track: Track) {
        val currentHistory = storage.getData() ?: arrayListOf()
        currentHistory.remove(track)
        currentHistory.add(0, track)

        val history = if (currentHistory.size > 10) {
            currentHistory.take(10)
        } else {
            currentHistory
        }

        storage.storeData(history as ArrayList<Track>)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }

    override fun clearHistory() {
        storage.clearData()
    }

    override fun getLastTrack(): Track? {
        val tracks = getHistory().data
        return tracks?.first()
    }
}