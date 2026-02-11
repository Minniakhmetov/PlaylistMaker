package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import androidx.core.net.toUri

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_activity)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val buttonBack = findViewById<MaterialToolbar>(R.id.toolbar_settings)
        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val buttonShareTheApp = findViewById<MaterialTextView>(R.id.share_the_app)
        buttonShareTheApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/*"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_the_app_uri))
            val chosenIntent = Intent.createChooser(intent, getString(R.string.share_the_app))
            startActivity(chosenIntent)
        }

        val buttonWriteToSupport = findViewById<MaterialTextView>(R.id.write_to_support)
        buttonWriteToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = getString(R.string.write_to_support_uri).toUri()
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_text))
            startActivity(intent)
        }

        val buttonUserAgreement = findViewById<MaterialTextView>(R.id.user_agreement)
        buttonUserAgreement.setOnClickListener {
            val address = getString(R.string.user_agreement_uri).toUri()
            val intent = Intent(Intent.ACTION_VIEW, address)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(intent)
        }
    }
}
