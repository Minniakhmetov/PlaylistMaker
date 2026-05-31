package com.example.playlistmaker.history.di

import com.example.playlistmaker.history.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.history.domain.impl.SearchHistoryInteractorImpl
import org.koin.dsl.module

val historyInteractorModule = module {
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
}
