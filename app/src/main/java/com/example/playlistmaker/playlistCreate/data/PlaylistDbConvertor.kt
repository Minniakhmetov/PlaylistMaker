package com.example.playlistmaker.playlistCreate.data

import com.example.playlistmaker.playlistCreate.data.db.PlaylistEntity
import com.example.playlistmaker.playlistCreate.domain.models.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.pathImageCover,
            playlist.trackIds,
            playlist.numberTracks,
            playlist.created
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.pathImageCover,
            playlistEntity.trackIds,
            playlistEntity.numberTracks,
            playlistEntity.created
        )
    }
}
