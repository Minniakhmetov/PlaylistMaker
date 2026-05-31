package com.example.playlistmaker.history.di

import android.content.Context
import com.example.playlistmaker.history.data.StorageClient
import com.example.playlistmaker.history.data.dto.TrackDtoSharedPreferences
import com.example.playlistmaker.history.data.dto.storage.PrefsStorageClient
import com.example.playlistmaker.history.di.HistoryConstants.APP_SHARED_PREFERENCE
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val historyDataModule = module {
    single {
        androidContext()
            .getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<StorageClient<ArrayList<Track>>> {
        val type = object : TypeToken<ArrayList<TrackDtoSharedPreferences>>() {}.type
        PrefsStorageClient(get(), get(), type)
    }
}
