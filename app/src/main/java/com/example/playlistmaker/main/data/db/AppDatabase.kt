package com.example.playlistmaker.main.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favoriteTracks.data.db.TrackEntity
import com.example.playlistmaker.main.data.db.dao.TrackDao
import com.example.playlistmaker.player.data.db.TrackInPlaylistEntity
import com.example.playlistmaker.player.data.db.dao.TrackInPlaylistDao
import com.example.playlistmaker.playlistCreate.data.db.PlaylistEntity
import com.example.playlistmaker.playlistCreate.data.db.dao.PlaylistDao

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}
