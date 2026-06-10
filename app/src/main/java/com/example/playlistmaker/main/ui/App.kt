package com.example.playlistmaker.main.ui

import android.app.Application
import com.example.playlistmaker.history.di.historyDataModule
import com.example.playlistmaker.history.di.historyInteractorModule
import com.example.playlistmaker.history.di.historyRepositoryModule
import com.example.playlistmaker.main.di.mainViewModelModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchInteractorModule
import com.example.playlistmaker.search.di.searchRepositoryModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingDataModule
import com.example.playlistmaker.settings.di.settingInteractorModule
import com.example.playlistmaker.settings.di.settingRepositoryModule
import com.example.playlistmaker.settings.di.settingViewModelModule
import com.example.playlistmaker.sharing.di.sharingDataModule
import com.example.playlistmaker.sharing.di.sharingInteractorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                mainViewModelModule,

                searchDataModule,
                searchInteractorModule,
                searchRepositoryModule,
                searchViewModelModule,

                historyDataModule,
                historyRepositoryModule,
                historyInteractorModule,

                settingDataModule,
                settingInteractorModule,
                settingRepositoryModule,
                settingViewModelModule,

                sharingDataModule,
                sharingInteractorModule,

                playerViewModelModule,
            )
        }
    }
}
