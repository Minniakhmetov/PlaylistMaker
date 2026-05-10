package com.example.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.presentation.ui.audioPlayer.AudioPlayerActivity
import com.example.playlistmaker.presentation.ui.medicalLibrary.MedicalLibraryActivity
import com.example.playlistmaker.presentation.ui.search.SearchActivity
import com.example.playlistmaker.presentation.ui.setting.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor
    private lateinit var historyInteractor: HistoryInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        settingsInteractor = Creator.provideSettingsInteractor(this)
        historyInteractor = Creator.provideHistoryInteractor(this)

        when (settingsInteractor.getLastActivity()) {
            AudioPlayerActivity.Companion.ACTIVITY_AUDIO_PLAYER_KEY -> {
                val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                intentAudioPlayer.putExtra(
                    SearchActivity.Companion.TRACK_KEY,
                    historyInteractor.getLastTrack()
                )
                startActivity(intentAudioPlayer)
            }
        }

        val buttonSearch = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = View.OnClickListener {
            val displaySearch = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        buttonSearch.setOnClickListener(searchClickListener)

        val buttonMedicalLibrary = findViewById<Button>(R.id.medical_library)
        buttonMedicalLibrary.setOnClickListener {
            val displayMedicalLibrary =
                Intent(this@MainActivity, MedicalLibraryActivity::class.java)
            startActivity(displayMedicalLibrary)
        }

        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonSettings.setOnClickListener {
            val displaySettings = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
    }

    override fun onStop() {
        super.onStop()
        settingsInteractor.saveLastActivity(ACTIVITY_MAIN_KEY)
    }

    companion object {
        const val ACTIVITY_MAIN_KEY = "key_for_main_activity"
    }
}
