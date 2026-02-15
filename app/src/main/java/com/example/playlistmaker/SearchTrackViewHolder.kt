package com.example.playlistmaker

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchTrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_search_track, parent, false)
) {
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvTrackArtist: TextView = itemView.findViewById(R.id.tvTrackArtist)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val imTrackIcon: ImageView = itemView.findViewById(R.id.imTrackIcon)

    fun bind(track: Track){
        tvTrackName.text = track.trackName
        tvTrackArtist.text = track.artistName
        tvTrackTime.text = track.trackTime
        Glide
            .with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                itemView.context.resources.displayMetrics).toInt()
            ))
            .into(imTrackIcon)
    }
}