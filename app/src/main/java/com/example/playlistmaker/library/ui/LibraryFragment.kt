package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.main.ui.theme.FirstComposeProjectTheme

class LibraryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            FirstComposeProjectTheme {
                LibraryScreen(
                    onTrackClick = { track ->
                        val action =
                            LibraryFragmentDirections.actionLibraryFragmentToAudioPlayerFragment(
                                track
                            )
                        findNavController().navigate(action)
                    },
                    onPlaylistClick = { playlist ->
                        val action =
                            LibraryFragmentDirections.actionLibraryFragmentToPlaylistInfoFragment(
                                playlist.id
                            )
                        findNavController().navigate(action)
                    },
                    onClickNewPlaylist = {
                        val action =
                            LibraryFragmentDirections.actionLibraryFragmentToPlaylistCreateFragment()
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}
