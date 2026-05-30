package com.example.playlistmaker.sharing.data

import android.content.res.Resources
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.util.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resources: Resources,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink(), getShareAppTitle())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resources.getString(R.string.share_the_app_uri)
    }

    private fun getShareAppTitle(): String {
        return resources.getString(R.string.share_the_app)
    }

    private fun getSupportEmailData(): EmailData {
        val addresses = arrayOf(resources.getString(R.string.write_to_support_email))

        return EmailData(
            addresses = addresses,
            data = "mailto:",
            subject = resources.getString(R.string.write_to_support_subject),
            supportText = resources.getString(R.string.write_to_support_text)
        )
    }

    private fun getTermsLink(): String {
        return resources.getString(R.string.user_agreement_uri)
    }
}
