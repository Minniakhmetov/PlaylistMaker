package com.example.playlistmaker.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.playlistCreate.domain.models.Playlist

class PlayerPlaylistsAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlayerPlaylistsViewHolder>() {
    var playlists = mutableListOf<Playlist>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlayerPlaylistsViewHolder = PlayerPlaylistsViewHolder.Companion.from(parent)

    override fun onBindViewHolder(
        holder: PlayerPlaylistsViewHolder,
        position: Int,
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(playlists[position])
        }
    }

    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: Playlist)
    }
}
