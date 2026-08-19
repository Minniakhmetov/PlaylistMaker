package com.example.playlistmaker.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.LibraryFragmentDirections
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_TRACK_DEBOUNCE_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true
    private val playlistsAdapter = PlaylistsAdapter { playlist ->
        if (clickDebounce()) {
            val action = LibraryFragmentDirections.actionLibraryFragmentToPlaylistInfoFragment(playlist.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = playlistsAdapter
        binding.playlistsMessageButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreateFragment)
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
            lifecycleScope.launch {
                delay(CLICK_TRACK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    fun showContent(playlists: List<Playlist>) {
        binding.messagePlaylists.isVisible = false
        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    fun showEmpty() {
        binding.messagePlaylists.isVisible = true
        playlistsAdapter.playlists.clear()
        playlistsAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {

        }
    }
}