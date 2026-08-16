package com.example.playlistmaker.playlists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.main.ui.utils.TextManager
import com.example.playlistmaker.playlistCreate.domain.models.Playlist

class PlaylistsViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(playlist: Playlist) {
        Glide
            .with(binding.root)
            .load(playlist.pathImageCover)
            .placeholder(R.drawable.ic_track_placeholder_312)
            .into(binding.imgPlaylist)

        binding.tvName.text = playlist.name
        binding.tvCount.text = TextManager.getCorrectEndingTextTrack(itemView.context, playlist.numberTracks)
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaylistBinding.inflate(inflater, parent, false)
            return PlaylistsViewHolder(binding)
        }
    }
}
