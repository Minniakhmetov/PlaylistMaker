package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { (messageCommunicationProblems: String, messageNothingWasFound: String) ->
        SearchViewModel(
            messageCommunicationProblems,
            messageNothingWasFound,
            get(),
            get(),
        )
    }
}