package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.audioPlayerActivity) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.observeProgressTime().observe(viewLifecycleOwner) {
            binding.tvAudioPlayerTrackTime.text = it
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            changeButtonImg(it == PlayerState.PLAYING)
        }

        viewModel.observeTrackIsFavorite().observe(viewLifecycleOwner) {
            changeTrackIsFavoriteButtonImg(it)
        }

        binding.tvAudioPlayerTrackTime.text = getString(R.string.track_start_time)

        binding.imgAudioPlayerTrackPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.imgAudioPlayerTrackLike.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeButtonImg(isPlaying: Boolean) {
        if (isPlaying) {
            binding.imgAudioPlayerTrackPlay.setImageResource(R.drawable.ic_track_pause_100)
        } else {
            binding.imgAudioPlayerTrackPlay.setImageResource(R.drawable.ic_track_play_100)
        }
    }

    private fun changeTrackIsFavoriteButtonImg(trackIsFavorite: Boolean) {
        if (trackIsFavorite) {
            binding.imgAudioPlayerTrackLike.setImageResource(R.drawable.ic_track_like_true_51)
        } else {
            binding.imgAudioPlayerTrackLike.setImageResource(R.drawable.ic_track_like_51)
        }
    }

    private fun setupTrack(track: Track) {
        Glide
            .with(this)
            .load(getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_track_placeholder_312)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8f,
                        this.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.audioPlayerTrackImg)
        binding.tvAudioPlayerTrackName.text = track.trackName
        binding.tvAudioPlayerTrackArtist.text = track.artistName
        val trackTime = track.trackTime
        binding.tvAudioPlayerValueDuration.text = getCoverTimeMillis(trackTime.toLong())

        if (track.collectionName.isNotEmpty()) {
            binding.tvAudioPlayerValueAlbum.text = track.collectionName
            binding.tvAudioPlayerTextAlbum.isVisible = true
        } else {
            binding.tvAudioPlayerTextAlbum.isVisible = false
        }
        if (track.releaseDate.isNotEmpty()) {
            binding.tvAudioPlayerValueReleaseDate.text = getCoverReleaseDate(track.releaseDate)
            binding.tvAudioPlayerTextReleaseDate.isVisible = true
        } else {
            binding.tvAudioPlayerTextReleaseDate.isVisible = false
        }
        binding.tvAudioPlayerValueGenre.text = track.primaryGenreName
        binding.tvAudioPlayerValueCountry.text = track.country
    }

    fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getCoverTimeMillis(time: Long): String? =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    fun getCoverReleaseDate(releaseDate: String): String? = releaseDate.take(4)

    fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.ShowTrack -> showTrack(state.track)
        }
    }

    fun showTrack(track: Track) {
        setupTrack(track)
    }

}
