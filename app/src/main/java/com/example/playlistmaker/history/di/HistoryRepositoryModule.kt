package com.example.playlistmaker.history.di

import com.example.playlistmaker.history.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.history.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val historyRepositoryModule = module {

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

}