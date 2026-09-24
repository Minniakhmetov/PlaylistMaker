package com.example.playlistmaker.playlistEdit.ui

import android.net.Uri
import android.widget.ImageView
import com.example.playlistmaker.R
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : PlaylistCreateFragment() {
    override val viewModel: PlaylistEditViewModel by viewModel()
    override fun showContent(playlist: Playlist, uri: Uri?) {
        binding.imgCreatePlaylistAddPhoto.background = null

        if (playlist.pathImageCover != null) {
            binding.imgCreatePlaylistAddPhoto.setImageURI(uri)
            binding.imgCreatePlaylistAddPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        binding.etPlaylistName.setText(playlist.name)
        binding.etPlaylistDescription.setText(playlist.description)
    }

    override fun loading() {
        binding.toolbarCreatePlaylist.setTitle(getString(R.string.edit))
        binding.btnCreatePlaylist.text = getString(R.string.save)
    }
}
