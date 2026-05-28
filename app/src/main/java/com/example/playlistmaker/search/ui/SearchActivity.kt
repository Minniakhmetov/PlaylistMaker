package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
        const val TRACK_KEY = "key_for_track"
    }

    private var viewModel: SearchViewModel? = null

    private val tracksAdapter = SearchTracksAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, it)
            viewModel?.onClickTrack(it)
            startActivity(intent)
        }
    }

    private val historyTracksAdapter = SearchTracksAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, it)
            viewModel?.onClickTrackHistory(it)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel =
            ViewModelProvider(this, SearchViewModel.getFactory()).get(SearchViewModel::class.java)
        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        binding.tracksList.layoutManager = LinearLayoutManager(this)
        binding.tracksList.adapter = tracksAdapter
        binding.historyTracks.layoutManager = LinearLayoutManager(this)
        binding.historyTracks.adapter = historyTracksAdapter

        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel?.clearHistory()
        }

        binding.messageButton.setOnClickListener {
            viewModel?.repeatLastSearch()
        }

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        binding.buttonClearSearch.setOnClickListener {
            viewModel?.removeLatestSearchText()
            binding.inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            tracksAdapter.tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            binding.messagePlaceholder.isVisible = false
        }

        binding.inputTextSearch.doOnTextChanged { text, start, before, count ->
            binding.buttonClearSearch.isVisible = buttonClearSearchVisibility(text)
            viewModel?.searchDebounce(
                changedText = text.toString()
            )
        }
        binding.inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputTextSearch.text.isEmpty() && !(binding.messagePlaceholder.isVisible)) {
                viewModel?.searchDebounce(
                    changedText = ""
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_TRACK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun showStart() {
        tracksAdapter.tracks.clear()
        tracksAdapter.notifyDataSetChanged()
        historyTracksAdapter.tracks.clear()
        historyTracksAdapter.notifyDataSetChanged()
        binding.llSearchHistory.isVisible = false
    }

    fun showLoading() {
        binding.llSearchHistory.isVisible = false
        binding.messagePlaceholder.isVisible = false
        binding.messageButton.isVisible = false
        binding.pbTracksSearch.isVisible = true
    }

    fun showContent(tracksList: List<Track>) {
        binding.pbTracksSearch.isVisible = false
        binding.messagePlaceholder.isVisible = false
        binding.messageButton.isVisible = false
        binding.llSearchHistory.isVisible = false
        tracksAdapter.tracks.clear()
        tracksAdapter.tracks.addAll(tracksList)
        tracksAdapter.notifyDataSetChanged()
    }

    fun showContentHistory(tracksList: List<Track>) {
        binding.pbTracksSearch.isVisible = false
        binding.messagePlaceholder.isVisible = false
        binding.messageButton.isVisible = false
        binding.llSearchHistory.isVisible = true
        tracksAdapter.tracks.clear()
        tracksAdapter.notifyDataSetChanged()
        historyTracksAdapter.tracks.clear()
        historyTracksAdapter.tracks.addAll(tracksList)
        historyTracksAdapter.notifyDataSetChanged()
    }

    fun showError(errorMessage: String) {
        binding.pbTracksSearch.isVisible = false
        binding.messagePlaceholder.isVisible = true
        binding.messageText.text = errorMessage
        binding.messageButton.isVisible = true
        binding.messageImage.setImageResource(R.drawable.ic_communication_problems_120)
        binding.llSearchHistory.isVisible = false
        tracksAdapter.tracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    fun showEmpty(emptyMessage: String) {
        binding.pbTracksSearch.isVisible = false
        binding.messagePlaceholder.isVisible = true
        binding.messageText.text = emptyMessage
        binding.messageButton.isVisible = false
        binding.messageImage.setImageResource(R.drawable.ic_nothing_was_found_120)
        binding.llSearchHistory.isVisible = false
        tracksAdapter.tracks.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.ContentHistory -> showContentHistory(state.tracks)
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Empty -> showEmpty(state.message)
            SearchState.Start -> showStart()
        }
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }
}
