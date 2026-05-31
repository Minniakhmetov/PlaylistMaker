package com.example.playlistmaker.history.data


import com.example.playlistmaker.history.data.dto.TrackDtoSharedPreferences
import com.example.playlistmaker.history.data.extension.toDomainModel
import com.example.playlistmaker.history.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.extension.toTrackDtoSharedPreferencesModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<TrackDtoSharedPreferences>>,
) : SearchHistoryRepository {

    override fun saveToHistory(track: Track) {
        val currentHistory = storage.getData(SEARCH_HISTORY_KEY) ?: arrayListOf()
        currentHistory.remove(track.toTrackDtoSharedPreferencesModel())
        currentHistory.add(0, track.toTrackDtoSharedPreferencesModel())
        val history = if (currentHistory.size > HISTORY_MAX_SIZE) {
            currentHistory.take(HISTORY_MAX_SIZE)
        } else {
            currentHistory
        }

        storage.storeData(SEARCH_HISTORY_KEY, history as ArrayList<TrackDtoSharedPreferences>)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData(SEARCH_HISTORY_KEY) ?: listOf()
        val tracksDomain = tracks.map {
            it.toDomainModel()
        }
        return Resource.Success(tracksDomain)
    }

    override fun clearHistory() {
        storage.clearData(SEARCH_HISTORY_KEY)
    }

    override fun getLastTrack(): Track? {
        val tracks = getHistory().data
        return tracks?.first()
    }

    companion object {
        const val HISTORY_MAX_SIZE = 10
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}
