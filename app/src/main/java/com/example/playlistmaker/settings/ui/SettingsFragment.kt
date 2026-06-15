package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment: Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareTheApp.setOnClickListener {
            viewModel.shareApp()
        }
        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.openLink()
        }
        binding.themeSwitcherDarkTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSetting(checked)
        }
        viewModel.observeThemeSettings().observe(viewLifecycleOwner) {
            binding.themeSwitcherDarkTheme.isChecked = it.darkTheme
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
