package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingInteractorModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext().resources)
    }
}