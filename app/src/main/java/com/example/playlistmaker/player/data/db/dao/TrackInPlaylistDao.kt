package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.player.data.db.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(trackInPlaylist: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist")
    fun getTracksInPlaylist(): Flow<List<TrackInPlaylistEntity>>

    @Query ("DELETE FROM track_in_playlist WHERE trackId = :trackId" )
    suspend fun deleteTrack(trackId: Long): Int

}