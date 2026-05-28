package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.util.EmailData

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {
    override fun shareLink(link: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/*"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        val chosenIntent = Intent.createChooser(intent, title)
        chosenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chosenIntent)
    }

    override fun openLink(link: String) {
        val address = link.toUri()
        val intent = Intent(Intent.ACTION_VIEW, address).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = emailData.data.toUri()
            putExtra(Intent.EXTRA_EMAIL, emailData.addresses)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.supportText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
