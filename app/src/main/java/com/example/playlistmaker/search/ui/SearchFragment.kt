package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.theme.FirstComposeProjectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            FirstComposeProjectTheme {
                SearchScreen(
                    viewModel,
                    onTrackClick = { track ->
                        if (clickDebounce()) {
                            val action =
                                SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(
                                    track
                                )
                            findNavController().navigate(action)
                            viewModel.onClickTrack(track)
                        }
                    },
                    onClickClearHistory = {
                        viewModel.clearHistory()
                    },
                )
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_TRACK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}
