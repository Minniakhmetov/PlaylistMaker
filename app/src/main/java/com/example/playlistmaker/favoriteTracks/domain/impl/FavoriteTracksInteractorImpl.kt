package com.example.playlistmaker.favoriteTracks.domain.impl

import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {

    override suspend fun saveFavoriteTrack(track: Track) {
        favoriteTracksRepository.saveFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracksRepository.deleteFavoriteTrack(track)
    }

    override fun getFavoriteTracksFlow(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracksFlow()
    }
}