package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.main.ui.theme.FirstComposeProjectTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val themeSettings by viewModel.themeSettings.collectAsState()

            FirstComposeProjectTheme {
                SettingsScreen(
                    checked = themeSettings.darkTheme,
                    onCheckedChange = viewModel::updateThemeSetting,
                    onClickShareApp = { viewModel.shareApp() },
                    onClickOpenLink = { viewModel.openLink() },
                    onClickOpenSupport = { viewModel.openSupport() }
                )
            }
        }
    }
}
