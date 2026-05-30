package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsInteractor: SettingsInteractor

    private var viewModel: SettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.settingsActivity) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        viewModel = ViewModelProvider(this, SettingsViewModel.getFactory())
            .get(SettingsViewModel::class.java)

        settingsInteractor = Creator.provideSettingsInteractor(this)

        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        binding.shareTheApp.setOnClickListener {
            viewModel?.shareApp()
        }
        binding.writeToSupport.setOnClickListener {
            viewModel?.openSupport()
        }
        binding.userAgreement.setOnClickListener {
            viewModel?.openLink()
        }

        binding.themeSwitcherDarkTheme.setOnCheckedChangeListener { themeSwitcher, checked ->
            viewModel?.updateThemeSetting(checked)
        }

        viewModel?.observeThemeSettings()?.observe(this) {
            binding.themeSwitcherDarkTheme.isChecked = it.darkTheme
            settingsInteractor.installTheme()
        }
    }
}
