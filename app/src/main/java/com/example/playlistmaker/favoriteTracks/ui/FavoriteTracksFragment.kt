package com.example.playlistmaker.favoriteTracks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }

    fun showEmpty() {
        binding.messageFavoriteTracks.isVisible = true
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment().apply {}
    }
}