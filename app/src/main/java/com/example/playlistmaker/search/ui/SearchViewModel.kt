package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val messageCommunicationProblems: String,
    private val messageNothingWasFound: String,
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: SearchHistoryInteractor,

    ) : ViewModel() {
    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchDebounce(changedText: String) {
        if (changedText.isEmpty()) {
            if (latestSearchText == null) {
                loadHistory()
                return
            }
        }

        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?){
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                renderState(
                    SearchState.Error(
                        errorMessage = messageCommunicationProblems
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    SearchState.Empty(
                        message = messageNothingWasFound
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

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchState.Loading
            )

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
