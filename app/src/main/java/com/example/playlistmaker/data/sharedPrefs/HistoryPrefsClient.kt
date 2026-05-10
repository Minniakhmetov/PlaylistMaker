package com.example.playlistmaker.data.sharedPrefs

import com.example.playlistmaker.data.dto.TrackDtoSharedPreferences


interface HistoryPrefsClient {
    val historyTracks: MutableList<TrackDtoSharedPreferences>
    fun saveTrack(track: TrackDtoSharedPreferences)
    fun clearHistory()
}
