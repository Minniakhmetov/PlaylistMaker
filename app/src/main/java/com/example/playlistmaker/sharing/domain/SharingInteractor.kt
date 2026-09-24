package com.example.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp()
    fun shareApp(title: String, message: String)
    fun openTerms()
    fun openSupport()
}
