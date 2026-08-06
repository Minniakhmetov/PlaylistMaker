package com.example.playlistmaker.main.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.favoriteTracks.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM favorite_tracks ORDER BY created DESC")
    fun getTracksFlow(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getTrackIds(): List<Int>
}
