package com.example.playlistmaker.favoriteTracks.domain.impl

import android.util.Log
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

    override fun getFavoriteTracks(): Flow<List<Track>> {
        Log.d("MyLog", "FavoriteTracksInteractorImpl - getFavoriteTracks:")
        return favoriteTracksRepository.getFavoriteTracks()
    }

    override fun getFavoriteTracksFlow(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracksFlow()
    }
}