package com.example.playlistmaker.main.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.library.ui.MedicalLibraryViewModel.Companion.ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY
import com.example.playlistmaker.library.ui.MedicalLibraryViewModel.Companion.ACTIVITY_LIBRARY_PLAYLISTS_KEY
import com.example.playlistmaker.settings.domain.SettingsInteractor


class MainActivityViewModel(
    private val historyInteractor: SearchHistoryInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<MainState>()
    fun observeState(): LiveData<MainState> = stateLiveData

    fun installTheme() {
        settingsInteractor.installTheme()
    }

    fun start() {
        when(val lastActivity = settingsInteractor.getLastActivity()){
            ACTIVITY_AUDIO_PLAYER_KEY -> {
                val lastTrack = historyInteractor.getLastTrack()
                if (lastTrack != null) {
                    renderState(MainState.OpenLastTrack(lastActivity, lastTrack))
                }
            }
            ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY, ACTIVITY_LIBRARY_PLAYLISTS_KEY -> {
                renderState(MainState.OpenMedicalLibraryActivity(lastActivity))
            }
        }
    }

    fun onClickSearch() {
        renderState(MainState.OpenSearchActivity)
    }

    fun onClickSetting() {
        renderState(MainState.OpenSettingsActivity)
    }

    fun onClickMedicalLibrary() {
        renderState(MainState.OpenMedicalLibraryActivity(""))
    }

    private fun renderState(state: MainState) {
        stateLiveData.postValue(state)
    }

    fun onStop() {
        settingsInteractor.saveLastActivity(ACTIVITY_MAIN_KEY)
    }

    fun onPause() {
        renderState(MainState.Start)
    }

    companion object {
        const val ACTIVITY_MAIN_KEY = "key_for_main_activity"
        const val ACTIVITY_AUDIO_PLAYER_KEY = "key_for_audio_player_activity"
    }
}
