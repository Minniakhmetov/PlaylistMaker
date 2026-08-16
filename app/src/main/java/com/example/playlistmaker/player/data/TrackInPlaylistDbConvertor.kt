package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.data.db.TrackInPlaylistEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackInPlaylistDbConvertor {
    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }

    fun map(trackInPlaylistEntity: TrackInPlaylistEntity): Track {
        return Track(
            trackInPlaylistEntity.trackId,
            trackInPlaylistEntity.trackName,
            trackInPlaylistEntity.artistName,
            trackInPlaylistEntity.trackTimeMillis,
            trackInPlaylistEntity.artworkUrl100,
            trackInPlaylistEntity.collectionName,
            trackInPlaylistEntity.releaseDate,
            trackInPlaylistEntity.primaryGenreName,
            trackInPlaylistEntity.country,
            trackInPlaylistEntity.previewUrl,
        )
    }
}