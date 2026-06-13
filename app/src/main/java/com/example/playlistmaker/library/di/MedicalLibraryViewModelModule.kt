package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.MedicalLibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medicalLibraryViewModelModule = module {
    viewModel {
        MedicalLibraryViewModel(get())
    }
}