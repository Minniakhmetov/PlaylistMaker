package com.example.playlistmaker.playlistEdit.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditFragment : PlaylistCreateFragment() {
    override val viewModel: PlaylistEditViewModel by viewModel()
    override fun showContent(playlist: Playlist) {
    binding.imgCreatePlaylistAddPhoto.background = null

        if (playlist.pathImageCover != null){
            val file = File(playlist.pathImageCover)
            Glide.with(binding.root)
                .load(file)
                .centerCrop()
                .into(binding.imgCreatePlaylistAddPhoto)
        }

        binding.etPlaylistName.setText(playlist.name)
        binding.etPlaylistDescription.setText(playlist.description)
    }

    override fun loading(){
        binding.toolbarCreatePlaylist.setTitle("Редактировать")
        binding.btnCreatePlaylist.text = "Сохранить"
    }
}
