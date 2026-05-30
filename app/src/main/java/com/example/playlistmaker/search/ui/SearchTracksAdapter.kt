package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.models.Track

class SearchTracksAdapter(private val clickListener: SearchTrackClickListener) :
    RecyclerView.Adapter<SearchTrackViewHolder>() {
    var tracks = mutableListOf<Track>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchTrackViewHolder = SearchTrackViewHolder.Companion.from(parent)

    override fun onBindViewHolder(
        holder: SearchTrackViewHolder,
        position: Int,
    ) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[position])
        }
    }

    override fun getItemCount() = tracks.size

    fun interface SearchTrackClickListener {
        fun onTrackClick(track: Track)
    }
}