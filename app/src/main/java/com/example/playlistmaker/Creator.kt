package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.HistoryManager
import com.example.playlistmaker.data.HistoryRepositoryImpl
import com.example.playlistmaker.data.SettingsManager
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }


    private fun getSettingsManager(context: Context): SettingsManager {
        return SettingsManager(context)
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsManager(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }


    private fun getHistoryManager(context: Context): HistoryManager {
        return HistoryManager(context)
    }

    private fun getSHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(getHistoryManager(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getSHistoryRepository(context))
    }

}
