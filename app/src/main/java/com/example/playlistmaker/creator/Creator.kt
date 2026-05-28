package com.example.playlistmaker.creator


import android.content.Context
import com.example.playlistmaker.history.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.history.data.dto.storage.PrefsStorageClient
import com.example.playlistmaker.history.data.dto.storage.PrefsStorageClient.Companion.SEARCH_HISTORY_KEY
import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.history.domain.api.SearchHistoryRepository
import com.example.playlistmaker.history.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.settings.data.SettingsInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.sharedPrefs.SettingsPrefsManager
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.google.gson.reflect.TypeToken

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }


    private fun getSettingsManager(context: Context): SettingsPrefsManager {
        return SettingsPrefsManager(context)
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsManager(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Track>>(
                context,
                SEARCH_HISTORY_KEY,
                object : TypeToken<ArrayList<Track>>() {}.type
            )
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context), context.resources)
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
}