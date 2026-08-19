package com.example.playlistmaker.playlists.data.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.main.data.db.AppDatabase
import com.example.playlistmaker.player.data.TrackInPlaylistDbConvertor
import com.example.playlistmaker.playlistEdit.data.PlaylistDbConvertor
import com.example.playlistmaker.playlistEdit.data.db.PlaylistEntity
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
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

    override suspend fun updatePlaylist(playlist: Playlist, uri: Uri?) {
        if (uri == null){
            val playlistEntity = playlistDbConvertor.map(playlist)
            appDatabase.playlistDao().updatePlaylist(playlistEntity)
        }else{
            val playlist = playlist.copy(
                created = System.currentTimeMillis(),
                pathImageCover = saveImageToPrivateStorage(playlist, uri)
            )
            val playlistEntity = playlistDbConvertor.map(playlist)
            appDatabase.playlistDao().updatePlaylist(playlistEntity)
        }

    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylistsFlow()
            .map { playlistEntity ->
                convertFromPlaylistEntity(playlistEntity)
            }
    }

    override fun getPlaylist(id: Long): Flow<Playlist?> {
        return appDatabase.playlistDao().getPlaylist(id)
    }

    override suspend fun deletePlaylist(playlist: Playlist): Boolean {
        appDatabase.playlistDao().deletePlaylist(playlist.id)
        if (playlist.trackIds != null){
            val type = object : TypeToken<List<Long>>() {}.type
            val trackIds = gson.fromJson<List<Long>>(playlist.trackIds, type)
            val result = deleteTracksNotInPlaylists(trackIds)
            return result
        }else{
            return true
        }
    }

    override suspend fun saveTrackInPlaylist(track: Track) {
        appDatabase.trackInPlaylistDao()
            .insertTrackInPlaylist(trackInPlaylistDbConvertor.map(track))
    }

    override suspend fun deleteTrackInPlaylist(playlist: Playlist, trackId: Long) {
        val type = object : TypeToken<MutableList<Long>>() {}.type
        val trackIdsList: MutableList<Long> = gson.fromJson(playlist.trackIds, type)
        trackIdsList.remove(trackId)
        var newTrackIdsList: String? = null
        if (trackIdsList.isNotEmpty()){
            newTrackIdsList = gson.toJson(trackIdsList)
        }
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist.copy(trackIds = newTrackIdsList, numberTracks = trackIdsList.size)))

        val trackIds: List<Long> = listOf(trackId)
        deleteTracksNotInPlaylists(trackIds)
    }

    private suspend fun deleteTracksNotInPlaylists(trackIds: List<Long>): Boolean{

        var result = false

        val playlistEntities = appDatabase.playlistDao().getPlaylists()
        val allPlaylists = playlistEntities.map { playlistEntity ->
            playlistDbConvertor.map(playlistEntity)
        }

        val trackIdsInPlaylists: MutableList<Long> = mutableListOf()
        allPlaylists.forEach { playlist ->
            val type = object : TypeToken<List<Long>>() {}.type
            if (playlist.trackIds != null){
                val trackIdsList: List<Long> = gson.fromJson(playlist.trackIds, type)
                trackIdsInPlaylists.addAll(trackIdsList)
            }

        }
        val trackIdsToDelete = trackIds.filter { id ->
            !trackIdsInPlaylists.contains(id)
        }
        if (trackIdsToDelete.isNotEmpty()) {
            trackIdsToDelete.forEach { trackId->
               result = appDatabase.trackInPlaylistDao().deleteTrack(trackId) > 0
            }
        }
        return result
    }


    override fun getTracksInPlaylist(idTracks: String): Flow<List<Track>> {
        val type = object : TypeToken<List<Long>>() {}.type
        val idTracksGson: List<Long> = gson.fromJson(idTracks, type)

        val allTracks = appDatabase.trackInPlaylistDao().getTracksInPlaylist()

        val tracksInPlaylist = allTracks.map { tracks ->

            val filterTracks = tracks.filter { it.trackId in idTracksGson }.map { trackInPlaylistEntity ->
                trackInPlaylistDbConvertor.map(trackInPlaylistEntity)
            }
            filterTracks.sortedBy { track ->
                idTracksGson.indexOf(track.trackId)
            }.reversed()
        }
        return tracksInPlaylist
    }

    override suspend fun addTrackInPlaylist(
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
