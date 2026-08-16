package com.example.playlistmaker.favoriteTracks.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface  FavoriteTracksRepository {

    suspend fun saveFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(track: Track)
    fun getFavoriteTracksFlow(): Flow<List<Track>>

}
