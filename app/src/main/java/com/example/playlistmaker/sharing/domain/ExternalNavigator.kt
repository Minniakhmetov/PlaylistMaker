package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.util.EmailData

interface ExternalNavigator {
    fun shareLink(link: String, title: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}
