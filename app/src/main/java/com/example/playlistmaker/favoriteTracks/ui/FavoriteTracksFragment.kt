package com.example.playlistmaker.favoriteTracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.library.ui.LibraryFragmentDirections
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_TRACK_DEBOUNCE_DELAY
import com.example.playlistmaker.search.ui.SearchTracksAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private var isClickAllowed = true
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val favoriteTracksAdapter = SearchTracksAdapter(
        clickListener = { track ->
            if (clickDebounce()) {
                val action = LibraryFragmentDirections.actionLibraryFragmentToAudioPlayerFragment(track)
                findNavController().navigate(action)
            }
        },
        longClickListener = { _ ->
            true
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoriteTracksList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteTracksList.adapter = favoriteTracksAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showContent(state.favoriteTracks)
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }

    fun showContent(favoriteTracks: List<Track>) {
        binding.favoriteTracksMessageImage.isVisible = false
        binding.favoriteTracksMessageText.isVisible = false
        binding.rvFavoriteTracksList.isVisible = true
        favoriteTracksAdapter.tracks.clear()
        favoriteTracksAdapter.tracks.addAll(favoriteTracks)
        favoriteTracksAdapter.notifyDataSetChanged()
    }

    fun showEmpty() {
        binding.favoriteTracksMessageImage.isVisible = true
        binding.favoriteTracksMessageText.isVisible = true
        binding.rvFavoriteTracksList.isVisible = false
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
        fun newInstance() = FavoriteTracksFragment().apply {}
    }
}