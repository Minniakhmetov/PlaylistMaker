package com.example.playlistmaker.main.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favoriteTracks.data.db.TrackEntity
import com.example.playlistmaker.main.data.db.dao.TrackDao

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

}