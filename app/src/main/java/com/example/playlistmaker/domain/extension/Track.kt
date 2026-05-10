package com.example.playlistmaker.domain.extension

import com.example.playlistmaker.data.dto.TrackDtoSharedPreferences
import com.example.playlistmaker.domain.models.Track

fun Track.toTrackDtoSharedPreferencesModel(): TrackDtoSharedPreferences {
    return TrackDtoSharedPreferences(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTime,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}
