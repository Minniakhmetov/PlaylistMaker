package com.example.playlistmaker.playlists.data.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.main.data.db.AppDatabase
import com.example.playlistmaker.player.data.TrackInPlaylistDbConvertor
import com.example.playlistmaker.playlistCreate.data.PlaylistDbConvertor
import com.example.playlistmaker.playlistCreate.data.db.PlaylistEntity
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackInPlaylistDbConvertor: TrackInPlaylistDbConvertor,
    private val context: Context,
    private val gson: Gson
) : PlaylistsRepository {

    override suspend fun savePlaylist(playlist: Playlist, uri: Uri?) {
        val newPlaylist = playlist.copy(
            created = System.currentTimeMillis(),
            pathImageCover = saveImageToPrivateStorage(playlist, uri)
        )
        val playlistEntity = playlistDbConvertor.map(newPlaylist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { playlistEntity ->
                convertFromPlaylistEntity(playlistEntity)
            }
    }

    override suspend fun saveTrackInPlaylist(track: Track) {
        appDatabase.trackInPlaylistDao()
            .insertTrackInPlaylist(trackInPlaylistDbConvertor.map(track))
    }

    override suspend fun updatePlaylist(
        track: Track,
        playlist: Playlist
    ): Boolean {
        var trackIds: MutableList<Long> = mutableListOf()
        if (playlist.trackIds == null) {
            trackIds.add(track.trackId)
        } else {
            val type = object : TypeToken<MutableList<Long>>() {}.type
            trackIds = gson.fromJson(playlist.trackIds, type)
            trackIds.add(track.trackId)
        }

        appDatabase.trackInPlaylistDao()
            .insertTrackInPlaylist(trackInPlaylistDbConvertor.map(track))

        val trackIdsString = gson.toJson(trackIds)
        val newPlaylist = playlist.copy(trackIds = trackIdsString, numberTracks = trackIds.size)

        val updated = appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(newPlaylist))
        return updated>0
    }


    private fun saveImageToPrivateStorage(playlist: Playlist, uri: Uri?): String? {
        if (uri == null) {
            return null
        } else {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath, "${playlist.created}.jpg")
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            return file.toString()
        }
    }

    private fun convertFromPlaylistEntity(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlistEntity ->
            playlistDbConvertor.map(playlistEntity)
        }
    }
}
