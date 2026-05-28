package com.example.playlistmaker.main.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator


class MainActivityViewModel(context: Context) : ViewModel() {
    companion object {

        const val ACTIVITY_MAIN_KEY = "key_for_main_activity"
        const val ACTIVITY_AUDIO_PLAYER_KEY = "key_for_audio_player_activity"


        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                MainActivityViewModel(app)
            }
        }
    }

    private val historyInteractor = Creator.provideSearchHistoryInteractor(context)
    private val settingsInteractor = Creator.provideSettingsInteractor(context)

    private val stateLiveData = MutableLiveData<MainState>()
    fun observeState(): LiveData<MainState> = stateLiveData

    fun start() {
        val lastActivity = settingsInteractor.getLastActivity()
        if (lastActivity == ACTIVITY_AUDIO_PLAYER_KEY) {
            val lastTrack = historyInteractor.getLastTrack()
            if (lastTrack != null) {
                renderState(MainState.OpenLastTrack(lastActivity, lastTrack))
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
        renderState(MainState.OpenMedicalLibraryActivity)
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

}
