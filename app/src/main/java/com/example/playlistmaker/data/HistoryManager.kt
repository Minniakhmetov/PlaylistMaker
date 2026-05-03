package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
    val gson = Gson()
    var historyTracks: MutableList<Track> = mutableListOf()
    private var listener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        updateHistoryTracks()
        listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                updateHistoryTracks()
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun saveTrack(track: Track) {
        if (historyTracks.contains(track)) {
            if (historyTracks.indexOf(track) != 0) {
                historyTracks.remove(track)
                historyTracks.add(0, track)
            }
        } else {
            historyTracks.add(0, track)
            while (historyTracks.size > 10) {
                historyTracks.removeAt(historyTracks.lastIndex)
            }
        }
        saveTracksHistory()
    }

    fun updateHistoryTracks() {
        val tracksHistoryJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, "")
        if (tracksHistoryJson != "") {
            val type = object : TypeToken<MutableList<Track>>() {}.type
            historyTracks = gson.fromJson(tracksHistoryJson, type)
        } else {
            historyTracks.clear()
        }
    }

    fun saveTracksHistory() {
        if (historyTracks.isNotEmpty()) {
            val tracksHistoryGson = gson.toJson(historyTracks)
            sharedPreferences.edit {
                putString(SEARCH_HISTORY_KEY, tracksHistoryGson)
            }
        }
    }

    fun clearHistory() {
        sharedPreferences.edit { remove(SEARCH_HISTORY_KEY) }
        updateHistoryTracks()
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}