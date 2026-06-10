package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingDataModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}
