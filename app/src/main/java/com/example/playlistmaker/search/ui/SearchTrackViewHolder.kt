package com.example.playlistmaker.search.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemSearchTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchTrackViewHolder(private val binding: ItemSearchTrackBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvTrackArtist.text = track.artistName
        binding.tvTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
        Glide
            .with(binding.root)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder_45)
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2f,
                        itemView.context.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.imTrackIcon)
    }

    companion object {
        fun from(parent: ViewGroup): SearchTrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchTrackBinding.inflate(inflater, parent, false)
            return SearchTrackViewHolder(binding)
        }
    }
}
