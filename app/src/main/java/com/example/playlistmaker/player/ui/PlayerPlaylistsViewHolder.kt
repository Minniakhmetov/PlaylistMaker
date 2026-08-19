package com.example.playlistmaker.player.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlayerPlaylistBinding
import com.example.playlistmaker.main.ui.utils.TextManager
import com.example.playlistmaker.playlistCreate.domain.models.Playlist

class PlayerPlaylistsViewHolder(private val binding: ItemPlayerPlaylistBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(playlist: Playlist) {
        Glide
            .with(binding.root)
            .load(playlist.pathImageCover)
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
            .into(binding.imgPlaylist)

        binding.tvName.text = playlist.name
        binding.tvCount.text = TextManager.getCorrectEndingTextTrack(itemView.context, playlist.numberTracks)
    }
    companion object {
        fun from(parent: ViewGroup): PlayerPlaylistsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlayerPlaylistBinding.inflate(inflater, parent, false)
            return PlayerPlaylistsViewHolder(binding)
        }
    }
}
