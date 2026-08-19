package com.example.playlistmaker.playlistEdit.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.playlistEdit.data.db.PlaylistEntity
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getPlaylistsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylist(id: Long): Flow<Playlist?>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query ("DELETE FROM playlists WHERE id = :playlistId" )
    suspend fun deletePlaylist(playlistId: Long)

}
