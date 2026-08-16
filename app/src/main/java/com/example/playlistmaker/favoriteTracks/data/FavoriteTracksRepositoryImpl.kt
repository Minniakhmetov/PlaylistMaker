package com.example.playlistmaker.favoriteTracks.data

import com.example.playlistmaker.favoriteTracks.data.db.TrackEntity
import com.example.playlistmaker.main.data.db.AppDatabase
import com.example.playlistmaker.favoriteTracks.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {

    override suspend fun saveFavoriteTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().deleteTrack(trackEntity.trackId)
    }

    override fun getFavoriteTracksFlow(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracksFlow()
            .map { trackEntities ->
                convertFromTrackEntity(trackEntities)
            }
    }

    private fun convertFromTrackEntity(tracksEntity: List<TrackEntity>): List<Track> {
        return tracksEntity.map { trackEntity ->
            trackDbConvertor.map(trackEntity)
        }
    }

}