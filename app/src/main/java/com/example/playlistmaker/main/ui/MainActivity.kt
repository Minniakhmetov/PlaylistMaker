package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.library.ui.MedicalLibraryActivity
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.installTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainActivity) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.search.setOnClickListener {
            viewModel.onClickSearch()
        }
        binding.settings.setOnClickListener {
            viewModel.onClickSetting()
        }
        binding.medicalLibrary.setOnClickListener {
            viewModel.onClickMedicalLibrary()
        }

        viewModel.start()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    fun render(state: MainState) {
        when (state) {
            MainState.Start -> {}
            is MainState.OpenLastTrack -> startLastTrack(state.track)
            MainState.OpenMedicalLibraryActivity -> startMedicalLibraryActivity()
            MainState.OpenSearchActivity -> startSearchActivity()
            MainState.OpenSettingsActivity -> startSettingsActivity()
        }
    }

    fun startLastTrack(track: Track) {
        val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
        intentAudioPlayer.putExtra(
            SearchActivity.TRACK_KEY,
            track
        )
        startActivity(intentAudioPlayer)
    }

    fun startMedicalLibraryActivity() {
        val displayMedicalLibrary =
            Intent(this@MainActivity, MedicalLibraryActivity::class.java)
        startActivity(displayMedicalLibrary)
    }

    fun startSearchActivity() {
        val displaySearch = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(displaySearch)
    }

    fun startSettingsActivity() {
        val displaySettings = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(displaySettings)
    }
}
