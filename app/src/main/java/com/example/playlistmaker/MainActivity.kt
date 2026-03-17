package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlistmaker.App.Companion.SETTING_PREFERENCES
import com.example.playlistmaker.AudioPlayerActivity.Companion.ACTIVITY_AUDIO_PLAYER_KEY
import com.example.playlistmaker.AudioPlayerActivity.Companion.SETTING_ACTIVITY_LAST
import com.example.playlistmaker.SearchActivity.Companion.SEARCH_PREFERENCES
import com.example.playlistmaker.SearchActivity.Companion.TRACK_KEY


class MainActivity : AppCompatActivity() {
    lateinit var sharedPrefsSetting: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        sharedPrefsSetting = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        when (sharedPrefsSetting.getString(SETTING_ACTIVITY_LAST, "")) {
            ACTIVITY_AUDIO_PLAYER_KEY -> {
                val sharedPrefs = getSharedPreferences(SEARCH_PREFERENCES, MODE_PRIVATE)
                val searchHistory = SearchHistory(sharedPrefs)
                val intentAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                intentAudioPlayer.putExtra(TRACK_KEY, searchHistory.historyTracks[0])
                startActivity(intentAudioPlayer)
            }
        }

        val buttonSearch = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displaySearch = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearch)
            }
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
        sharedPrefsSetting.edit {
            putString(SETTING_ACTIVITY_LAST, ACTIVITY_MAIN_KEY)
        }
    }

    companion object {
        const val ACTIVITY_MAIN_KEY = "key_for_main_activity"
    }
}
