package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.player.data.db.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(trackInPlaylist: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist")
    fun getTracksInPlaylist(): Flow<List<TrackInPlaylistEntity>>

}