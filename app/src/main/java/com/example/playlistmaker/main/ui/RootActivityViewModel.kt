package com.example.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor


class RootActivityViewModel(
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<RootActivityState>()
    fun observeState(): LiveData<RootActivityState> = stateLiveData
    fun installTheme() {
        settingsInteractor.installTheme()
    }

    fun saveLastDestination(id: Int) {
        settingsInteractor.saveLastDestinationId(id)
    }

    fun getLastDestinationId() {
        val lastDestinationId = settingsInteractor.getLastDestinationId()
        if (lastDestinationId != -1) {
            renderState(
                RootActivityState.OpenLastPage(
                    lastDestinationId = lastDestinationId
                )
            )
        }
    }

    private fun renderState(state: RootActivityState) {
        stateLiveData.postValue(state)
    }

}
