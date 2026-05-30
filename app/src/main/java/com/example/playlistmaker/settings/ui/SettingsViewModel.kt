package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.main.ui.App
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.util.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    context: Context,
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val themeSettings =
        MutableLiveData<ThemeSettings>(settingsInteractor.getThemeSettings())

    fun observeThemeSettings(): LiveData<ThemeSettings> = themeSettings

    fun updateThemeSetting(state: Boolean) {
        val newThemeSetting = ThemeSettings(state)
        settingsInteractor.updateThemeSetting(newThemeSetting)
        themeSettings.postValue(newThemeSetting)
    }

    fun openLink() {
        sharingInteractor.openTerms()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                val settingsInteractor = Creator.provideSettingsInteractor(app)
                val sharingInteractor = Creator.provideSharingInteractor(app)
                SettingsViewModel(app, settingsInteractor, sharingInteractor)
            }
        }
    }
}
