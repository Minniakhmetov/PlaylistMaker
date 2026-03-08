package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchTracksAdapter(private val onItemClick: (Int) -> Unit): RecyclerView.Adapter<SearchTrackViewHolder>() {
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
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
    override fun getItemCount() = tracks.size
}
