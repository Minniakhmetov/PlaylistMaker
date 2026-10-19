package com.example.playlistmaker.search.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val messageCommunicationProblems: String,
    private val messageNothingWasFound: String,
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: SearchHistoryInteractor,

    ) : ViewModel() {
    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private val _uiState = MutableStateFlow<SearchState>(SearchState.Start)
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    fun searchDebounce(changedText: String) {
        searchJob?.cancel()
        if (changedText.isEmpty()) {
            if (latestSearchText == null) {
                loadHistory()
                return
            }
        }
        latestSearchText = changedText
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
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
        viewModelScope.launch {
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
    }

    fun onClickTrack(track: Track) {
        saveTrack(track)
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
        _uiState.value = state
    }

    fun renderStateStart() {
        renderState(
            SearchState.Start
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
