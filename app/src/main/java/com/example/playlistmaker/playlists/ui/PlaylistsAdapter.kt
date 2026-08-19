package com.example.playlistmaker.playlists.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.playlistEdit.domain.models.Playlist

class PlaylistsAdapter (private val clickListener: PlaylistClickListener) :
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
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlists[position])
        }
    }

    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
