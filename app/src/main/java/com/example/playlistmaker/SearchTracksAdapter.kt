package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchTracksAdapter: RecyclerView.Adapter<SearchTrackViewHolder>() {
    var tracks = mutableListOf<Track>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchTrackViewHolder = SearchTrackViewHolder(parent)

    override fun onBindViewHolder(
        holder: SearchTrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
    }
    override fun getItemCount() = tracks.size
}
