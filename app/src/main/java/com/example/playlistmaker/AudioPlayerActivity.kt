package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    lateinit var sharedPrefs: SharedPreferences

    private lateinit var trackImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var trackArtist: TextView
    private lateinit var trackTime: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackAlbumText: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackReleaseDateText: TextView
    private lateinit var trackReleaseDate: TextView
    private lateinit var trackReleaseGenreText: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountryText: TextView
    private lateinit var trackCountry: TextView

    private lateinit var track: Track

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedPrefs.edit {
            putBoolean(ACTIVITY_AUDIO_PLAYER_KEY, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio_player_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        trackImage = findViewById(R.id.audio_player_track_img)
        trackName = findViewById(R.id.tv_audio_player_track_name)
        trackArtist = findViewById(R.id.tv_audio_player_track_artist)
        trackTime = findViewById(R.id.tv_audio_player_track_time)
        trackDuration = findViewById(R.id.tv_audio_player_value_duration)
        trackAlbumText = findViewById(R.id.tv_audio_player_text_album)
        trackAlbum = findViewById(R.id.tv_audio_player_value_album)
        trackReleaseDateText = findViewById(R.id.tv_audio_player_text_release_date)
        trackReleaseDate = findViewById(R.id.tv_audio_player_value_release_date)
        trackReleaseGenreText = findViewById(R.id.tv_audio_player_text_genre)
        trackGenre = findViewById(R.id.tv_audio_player_value_genre)
        trackCountryText = findViewById(R.id.tv_audio_player_text_country)
        trackCountry = findViewById(R.id.tv_audio_player_value_country)

        sharedPrefs = getSharedPreferences(ACTIVITY_PREFERENCES, MODE_PRIVATE)

        val buttonBack = findViewById<MaterialToolbar>(R.id.toolbar_audio_player)
        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val gson = Gson()
        val intentTrack = intent.getStringExtra("track") ?: ""
        if (intentTrack.isNotEmpty()){
            val type = object : TypeToken<Track>() {}.type
            track = gson.fromJson(intentTrack, type)
            Glide
                .with(this)
                .load(getCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.ic_track_placeholder_312)
                .transform(RoundedCorners(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8f,
                    this.resources.displayMetrics).toInt()
                ))
                .into(trackImage)
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            val trackTimeMillis = getCoverTimeMillis(track.trackTimeMillis.toLong())
            trackTime.text = trackTimeMillis
            trackDuration.text = trackTimeMillis

            if (track.collectionName.isNotEmpty()){
                trackAlbum.text = track.collectionName
                trackAlbumText.isVisible = true
            }else{
                trackAlbumText.isVisible = false
            }
            if (track.releaseDate.isNotEmpty()){
                trackReleaseDate.text = getCoverReleaseDate(track.releaseDate)
                trackReleaseDateText.isVisible = true
            }else{
                trackReleaseDateText.isVisible = false
            }
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country
        }
    }

    fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getCoverTimeMillis(time: Long): String? = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    fun getCoverReleaseDate(releaseDate: String): String? = releaseDate.take(4)

    companion object {
        const val ACTIVITY_PREFERENCES = "activity_preferences"
        const val ACTIVITY_AUDIO_PLAYER_KEY = "key_for_audio_player"
    }
}
