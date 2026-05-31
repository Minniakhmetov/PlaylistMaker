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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var track: Track

    private lateinit var binding: ActivityAudioPlayerBinding
    private var mainThreadHandler: Handler? = null


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


        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            finish()
        }

        track = IntentCompat.getParcelableExtra(
            intent,
            SearchActivity.Companion.TRACK_KEY, Track::class.java
        ) ?: return

        setupTrack(track)

        viewModel.observeProgressTime().observe(this) {
            binding.tvAudioPlayerTrackTime.text = it
        }

        viewModel.observePlayerState().observe(this) {
            changeButtonImg(it == PlayerState.PLAYING)
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
