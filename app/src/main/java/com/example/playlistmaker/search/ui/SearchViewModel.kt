package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SearchViewModel(
    private val context: Context,
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: SearchHistoryInteractor,
    private val settingInteractor: SettingsInteractor,

    ) : ViewModel() {
    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<SearchState>()

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchDebounce(changedText: String) {
        if (changedText.isEmpty()) {
            if (latestSearchText == null) {
                handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
                loadHistory()
                return
            }
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchState.Loading
            )

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    handler.post {
                        val tracks = mutableListOf<Track>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    SearchState.Error(
                                        errorMessage = context.getString(R.string.communication_problems),
                                    )
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    SearchState.Empty(
                                        message = context.getString(R.string.nothing_was_found),
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    SearchState.Content(
                                        tracks = tracks,
                                    )
                                )
                            }
                        }
                    }
                }
            })
        }
    }

    fun loadHistory() {
        historyInteractor.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(searchHistory: List<Track>?) {
                val tracks = mutableListOf<Track>()
                if (searchHistory?.isNotEmpty() ?: false) {
                    tracks.addAll(searchHistory)
                    renderState(
                        SearchState.ContentHistory(
                            tracks = tracks
                        )
                    )
                } else {
                    renderState(
                        SearchState.Start
                    )
                }
            }
        })
    }

    fun onClickTrack(track: Track) {
        saveTrack(track)
    }

    fun onClickTrackHistory(track: Track) {
        saveTrack(track)
        loadHistory()
    }

    fun removeLatestSearchText() {
        latestSearchText = null
    }

    fun repeatLastSearch() {
        latestSearchText?.let { searchDebounce(it) }
    }

    fun saveTrack(track: Track) {
        historyInteractor.saveToHistory(track)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        loadHistory()
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun onStop() {
        settingInteractor.saveLastActivity(ACTIVITY_SEARCH_KEY)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        const val ACTIVITY_SEARCH_KEY = "key_for_search_activity"
    }
}
