package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {
    private var isClickPlaylistAllowed = true
    private val viewModel:AudioPlayerViewModel  by viewModel{
        parametersOf(
            getString(R.string.message_track_already_added_playlist),
            getString(R.string.message_track_added_playlist)
        )
    }
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var bottomPadding = 0
    private val playlistsAdapter = PlayerPlaylistsAdapter { playlist ->
        if (clickDebounce()) {
            viewModel.onClickPlaylist(playlist)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observePlaylistsCurrent().observe(viewLifecycleOwner) {
            updatePlaylists(it)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.audioPlayerActivity) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomPadding = systemBars.bottom
            view.setPadding(0, 0, 0, bottomPadding)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistsBottomSheet) { view, insets ->
            view.setPadding(0, 0, 0, bottomPadding)
            insets
        }

        viewModel.initTrack()

        binding.rvPlayerPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayerPlaylists.adapter = playlistsAdapter

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

        viewModel.observeShowMessage().observe(viewLifecycleOwner){
            showToast(it)
        }
        viewModel.observeShowBottomSheet().observe(viewLifecycleOwner){
            showBottomSheet(it)
        }

        binding.tvAudioPlayerTrackTime.text = getString(R.string.track_start_time)

        binding.imgAudioPlayerTrackPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.imgAudioPlayerTrackLike.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.imgAudioPlayerTrackAddPlaylist.setOnClickListener {
            viewModel.trackAddPlaylistClicked()
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_playlistCreateFragment)
            viewModel.onClickCreateNewPlaylist()
        }


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 1 + slideOffset
            }

        })
        isClickPlaylistAllowed = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickPlaylistAllowed
        if (isClickPlaylistAllowed) {
            isClickPlaylistAllowed = false
            lifecycleScope.launch {
                delay(CLICK_PLAYLIST_DEBOUNCE_DELAY)
                isClickPlaylistAllowed = true
            }
        }
        return current
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

    fun getCoverReleaseDate(releaseDate: String): String = releaseDate.take(4)

    fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.ShowTrack -> showTrack(state.track)
        }
    }

    fun updatePlaylists(playlists: List<Playlist>) {
        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }


    fun showTrack(track: Track) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.overlay.isVisible = false
        setupTrack(track)
    }

    fun showBottomSheet(isShow: Boolean) {
        if (isShow){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.isVisible = true
        }
    }

    fun showToast(message: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.toast_custom, null)
        val textView = view.findViewById<TextView>(R.id.toastText)
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, bottomPadding)
        toast.show()
    }

    companion object {
        const val CLICK_PLAYLIST_DEBOUNCE_DELAY = 1000L
    }
}
