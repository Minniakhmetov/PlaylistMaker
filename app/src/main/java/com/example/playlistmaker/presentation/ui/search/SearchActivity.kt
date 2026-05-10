package com.example.playlistmaker.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.audioPlayer.AudioPlayerActivity
import com.google.android.material.appbar.MaterialToolbar


class SearchActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor
    private lateinit var historyInteractor: HistoryInteractor

    private val tracks = mutableListOf<Track>()
    private val tracksAdapter = SearchTracksAdapter { onItemClick(tracks[it]) }
    private val historyTracksAdapter =
        SearchTracksAdapter { onItemClick(historyInteractor.getTrack(it)) }

    private var textSearch: String = TEXT_SEARCH_VALUE
    private var lastTextSearch: String = ""

    private lateinit var messagePlaceholder: LinearLayout
    private lateinit var messageImage: ImageView
    private lateinit var messageText: TextView
    private lateinit var messageButton: Button
    private lateinit var inputTextSearch: EditText
    private lateinit var llSearchHistory: LinearLayout
    private lateinit var buttonClearHistory: Button
    private lateinit var pbTracksSearch: ProgressBar
    private lateinit var tracksInteractor: TracksInteractor


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH_KEY, textSearch)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        settingsInteractor = Creator.provideSettingsInteractor(this)
        tracksInteractor = Creator.provideTracksInteractor()
        historyInteractor = Creator.provideHistoryInteractor(this)

        messagePlaceholder = findViewById(R.id.message_placeholder)
        messageImage = findViewById(R.id.message_image)
        messageText = findViewById(R.id.message_text)
        messageButton = findViewById(R.id.message_button)
        llSearchHistory = findViewById(R.id.ll_search_history)
        buttonClearHistory = findViewById(R.id.btn_clear_history)
        pbTracksSearch = findViewById(R.id.pb_tracks_search)


        val buttonBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        buttonBack.setNavigationOnClickListener {
            finish()
        }

        inputTextSearch = findViewById(R.id.input_text_search)
        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(TEXT_SEARCH_KEY, TEXT_SEARCH_VALUE)
            if (textSearch.isNotEmpty()) {
                inputTextSearch.setText(textSearch)
            }
        }

        val buttonClearSearch = findViewById<ImageView>(R.id.button_clear_search)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        buttonClearSearch.setOnClickListener {
            inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            if (messagePlaceholder.isVisible) {
                showMessage("", "")
            }
            if (historyInteractor.getTracks().isNotEmpty()) {
                llSearchHistory.isVisible = true
            }
        }
        inputTextSearch.doOnTextChanged { text, start, before, count ->
            buttonClearSearch.isVisible = buttonClearSearchVisibility(text)
            textSearch = text.toString()

            llSearchHistory.isVisible = (inputTextSearch.hasFocus()
                    && text?.isEmpty() == true
                    && historyInteractor.getTracks().isNotEmpty()
                    && !(messagePlaceholder.isVisible))

            if (text?.isEmpty() == true) {
                tracks.clear()
                tracksAdapter.notifyDataSetChanged()
                historyTracksAdapter.tracks = historyInteractor.getTracks().toMutableList()
                historyTracksAdapter.notifyDataSetChanged()
            } else {
                searchTracksDebounce()
            }
        }

        val rvSearchTracks = findViewById<RecyclerView>(R.id.tracksList)
        rvSearchTracks.layoutManager = LinearLayoutManager(this)
        rvSearchTracks.adapter = tracksAdapter
        tracksAdapter.tracks = tracks

        inputTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (textSearch.isNotEmpty()) {
                    searchTracks(textSearch)
                    lastTextSearch = textSearch
                }
                true
            }
            false
        }
        inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            llSearchHistory.isVisible = (hasFocus
                    && inputTextSearch.text.isEmpty()
                    && historyInteractor.getTracks().isNotEmpty()
                    && !(messagePlaceholder.isVisible))
        }
        messageButton.setOnClickListener {
            searchTracks(lastTextSearch)
            inputTextSearch.setText(lastTextSearch)
        }

        val rvHistoryTracks = findViewById<RecyclerView>(R.id.history_tracks)
        rvHistoryTracks.layoutManager = LinearLayoutManager(this)
        rvHistoryTracks.adapter = historyTracksAdapter
        historyTracksAdapter.tracks = historyInteractor.getTracks().toMutableList()

        buttonClearHistory.setOnClickListener {
            historyInteractor.clearHistory()
            historyTracksAdapter.tracks = historyInteractor.getTracks().toMutableList()
            historyTracksAdapter.notifyDataSetChanged()
            llSearchHistory.isVisible = false
        }
    }

    override fun onStop() {
        super.onStop()
        settingsInteractor.saveLastActivity(ACTIVITY_SEARCH_KEY)
    }

    private fun searchTracks(searchTrack: String) {
        showMessage("", "")
        tracks.clear()
        tracksAdapter.notifyDataSetChanged()
        pbTracksSearch.isVisible = true

        val mainHandler = Handler(Looper.getMainLooper())

        tracksInteractor.searchTracks(searchTrack, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?) {
                mainHandler.post {
                    pbTracksSearch.isVisible = false
                    if (foundTracks != null) {
                        if (foundTracks.isNotEmpty()) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            tracksAdapter.notifyDataSetChanged()
                            showMessage("", "")
                        } else {
                            showMessage(getString(R.string.nothing_was_found), "")
                        }
                    } else {
                        showMessage(getString(R.string.communication_problems), "error")
                    }
                }
            }
        })
    }

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks(textSearch) }
    private fun searchTracksDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            messagePlaceholder.isVisible = true
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            messageText.text = text
            if (additionalMessage.isEmpty()) {
                messageButton.isVisible = false
                messageImage.setImageResource(R.drawable.ic_nothing_was_found_120)
            } else {
                messageButton.isVisible = true
                inputTextSearch.setText("")
                messageImage.setImageResource(R.drawable.ic_communication_problems_120)
            }
            llSearchHistory.isVisible = false
        } else {
            messagePlaceholder.isVisible = false
            messageButton.isVisible = false
        }
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun onItemClick(track: Track) {
        if (clickTrackDebounce()) {
            historyInteractor.saveTrack(track)
            historyTracksAdapter.tracks = historyInteractor.getTracks().toMutableList()
            historyTracksAdapter.notifyDataSetChanged()
            val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
            intentAudioPlayer.putExtra(TRACK_KEY, track)
            startActivity(intentAudioPlayer)
        }
    }

    private var isClickTrackAllowed = true
    private fun clickTrackDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            handler.postDelayed({ isClickTrackAllowed = true }, CLICK_TRACK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val TEXT_SEARCH_KEY = "TEXT_SEARCH"
        const val TEXT_SEARCH_VALUE = ""
        const val TRACK_KEY = "key_for_track"
        const val ACTIVITY_SEARCH_KEY = "key_for_search_activity"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}
