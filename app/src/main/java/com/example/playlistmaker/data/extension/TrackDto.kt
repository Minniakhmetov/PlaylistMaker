package com.example.playlistmaker.data.extension

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

fun TrackDto.toDomainModel(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong()),
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}