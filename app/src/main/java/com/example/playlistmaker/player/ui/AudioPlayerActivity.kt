package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.domain.SettingsInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel
    private var mainThreadHandler: Handler? = null
    private lateinit var settingsInteractor: SettingsInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.audioPlayerActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        settingsInteractor = Creator.provideSettingsInteractor(this)

        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            finish()
        }

        val track = IntentCompat.getParcelableExtra(
            intent,
            SearchActivity.Companion.TRACK_KEY, Track::class.java
        )

        if (track != null) {
            setupTrack(track)
            viewModel = ViewModelProvider(this, AudioPlayerViewModel.getFactory(track)).get(
                AudioPlayerViewModel::class.java
            )

            viewModel.observeProgressTime().observe(this) {
                binding.tvAudioPlayerTrackTime.text = it
            }

            viewModel.observePlayerState().observe(this) {
                changeButtonImg(it == AudioPlayerViewModel.STATE_PLAYING)
            }

        }

        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.tvAudioPlayerTrackTime.text = getString(R.string.track_start_time)

        binding.imgAudioPlayerTrackPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    private fun changeButtonImg(isPlaying: Boolean) {
        if (isPlaying) {
            binding.imgAudioPlayerTrackPlay.setImageResource(R.drawable.ic_track_pause_100)
        } else {
            binding.imgAudioPlayerTrackPlay.setImageResource(R.drawable.ic_track_play_100)
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

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

}
