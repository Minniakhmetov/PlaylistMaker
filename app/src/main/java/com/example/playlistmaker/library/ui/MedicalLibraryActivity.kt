package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMedicalLibraryBinding
import com.example.playlistmaker.library.ui.MedicalLibraryViewModel.Companion.ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY
import com.example.playlistmaker.library.ui.MedicalLibraryViewModel.Companion.ACTIVITY_LIBRARY_PLAYLISTS_KEY
import com.example.playlistmaker.main.ui.MainActivity.Companion.LIBRARY_PAGE_KEY
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicalLibraryActivity : AppCompatActivity(), SelectPage {
    private val viewModel by viewModel<MedicalLibraryViewModel>()
    private lateinit var binding: ActivityMedicalLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.medicalLibraryActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarMedicalLibrary.setNavigationOnClickListener {
            finish()
        }

        binding.viewPagerMedicalLibrary.adapter =
            FragmentsAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(
            binding.tabLayoutMedicalLibrary,
            binding.viewPagerMedicalLibrary
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.favorite_tracks)
                }

                1 -> {
                    tab.text = getString(R.string.playlists)
                }
            }

        }
        tabMediator.attach()

        val lastPage = intent.getStringExtra(LIBRARY_PAGE_KEY).toString()
        if (lastPage == ACTIVITY_LIBRARY_FAVORITE_TRACKS_KEY) {
            navigateTo(page = 0)
        } else if (lastPage == ACTIVITY_LIBRARY_PLAYLISTS_KEY) {
            navigateTo(page = 1)
        }
    }

    override fun navigateTo(page: Int) {
        binding.viewPagerMedicalLibrary.currentItem = page
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveLastPage(binding.viewPagerMedicalLibrary.currentItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
