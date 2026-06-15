package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel {
        parametersOf(
            getString(R.string.communication_problems),
            getString(R.string.nothing_was_found)
        )
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val tracksAdapter = SearchTracksAdapter { track ->
        if (clickDebounce()) {
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment)
            viewModel.onClickTrack(track)
        }
    }
    private val historyTracksAdapter = SearchTracksAdapter { track ->
        if (clickDebounce()) {
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment)
            viewModel.onClickTrackHistory(track)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksList.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksList.adapter = tracksAdapter
        binding.historyTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.historyTracks.adapter = historyTracksAdapter

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.messageButton.setOnClickListener {
            viewModel.repeatLastSearch()
        }

        val inputMethodManager =
            activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        binding.buttonClearSearch.setOnClickListener {
            viewModel.removeLatestSearchText()
            binding.inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            tracksAdapter.tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            binding.messagePlaceholder.isVisible = false
        }

        binding.inputTextSearch.doOnTextChanged { text, start, before, count ->
            binding.buttonClearSearch.isVisible = buttonClearSearchVisibility(text)
            viewModel.searchDebounce(
                changedText = text.toString()
            )
        }

        binding.inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputTextSearch.text.isEmpty() && !(binding.messagePlaceholder.isVisible)) {
                viewModel.searchDebounce(
                    changedText = ""
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_TRACK_DEBOUNCE_DELAY)
        }
        return current
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

    private fun buttonClearSearchVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}
