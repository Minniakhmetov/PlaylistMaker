package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(androidContext(), get(), get(), get())
    }
}