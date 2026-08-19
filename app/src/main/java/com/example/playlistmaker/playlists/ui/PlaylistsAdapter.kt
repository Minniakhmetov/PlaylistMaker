package com.example.playlistmaker.playlists.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.playlistCreate.domain.models.Playlist

class PlaylistsAdapter :
    RecyclerView.Adapter<PlaylistsViewHolder>() {
    var playlists = mutableListOf<Playlist>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaylistsViewHolder = PlaylistsViewHolder.Companion.from(parent)

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
        position: Int,
    ) {
        holder.bind(playlists[position])
    }

    override fun getItemCount() = playlists.size

}
