package com.example.playlistmaker.data

import com.example.playlistmaker.data.extension.toDomainModel
import com.example.playlistmaker.data.sharedPrefs.HistoryPrefsClient
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.extension.toTrackDtoSharedPreferencesModel
import com.example.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val historyPrefsClient: HistoryPrefsClient) :
    HistoryRepository {
    override fun getLastTrack(): Track {
        return historyPrefsClient.historyTracks[0].toDomainModel()
    }

    override fun getTrack(id: Int): Track {
        return historyPrefsClient.historyTracks[id].toDomainModel()
    }

    override fun getTracks(): List<Track> {
        return historyPrefsClient.historyTracks.map {
            it.toDomainModel()
        }
    }

    override fun clearHistory() {
        historyPrefsClient.clearHistory()
    }

    override fun saveTrack(track: Track) {
        historyPrefsClient.saveTrack(track.toTrackDtoSharedPreferencesModel())
    }
}
