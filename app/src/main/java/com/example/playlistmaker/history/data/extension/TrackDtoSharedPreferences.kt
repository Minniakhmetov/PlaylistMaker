package com.example.playlistmaker.history.data.extension

import com.example.playlistmaker.history.data.dto.TrackDtoSharedPreferences
import com.example.playlistmaker.search.domain.models.Track

fun TrackDtoSharedPreferences.toDomainModel(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}
