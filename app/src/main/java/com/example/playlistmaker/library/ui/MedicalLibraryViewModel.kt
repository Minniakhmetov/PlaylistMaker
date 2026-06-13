package com.example.playlistmaker.library.ui


import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class MedicalLibraryViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    fun saveLastPage(lastPage: Int) {
        if (lastPage == 0) {
            settingsInteractor.saveLastActivity(ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY)
        } else if (lastPage == 1) {
            settingsInteractor.saveLastActivity(ACTIVITY_LIBRARY_PLAYLISTS_KEY)
        }

    }

    companion object {
        const val ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY = "key_for_library_activity_favorite_tracks"
        const val ACTIVITY_LIBRARY_PLAYLISTS_KEY = "key_for_library_activity_playlists"
    }
}
