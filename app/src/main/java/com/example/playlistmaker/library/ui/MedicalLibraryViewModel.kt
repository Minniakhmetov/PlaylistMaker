package com.example.playlistmaker.library.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class MedicalLibraryViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<LibraryState>()
    fun observeState(): LiveData<LibraryState> = stateLiveData
    fun saveLastPage(lastPage: Int) {
        settingsInteractor.saveLastPage(lastPage)
    }

    fun getLastPage() {
        val lastPage = settingsInteractor.getLastPage()
        if (lastPage != -1) {
            renderState(
                LibraryState.OpenLastPage(
                    lastPageId = lastPage
                )
            )

        }
    }

    private fun renderState(state: LibraryState) {
        stateLiveData.postValue(state)
    }
}
