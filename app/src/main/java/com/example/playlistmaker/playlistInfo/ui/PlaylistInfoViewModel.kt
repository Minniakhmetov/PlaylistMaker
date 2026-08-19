package com.example.playlistmaker.playlistInfo.ui

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.utils.SingleLiveEvent
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.domain.db.PlaylistsInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {
    private val isShowBottomSheetMenu = MutableLiveData(false)
    fun observeShowBottomSheetMenu(): LiveData<Boolean> = isShowBottomSheetMenu

    private val isShowDeletePlaylistDialog = SingleLiveEvent<Playlist>()
    fun observeShowDeletePlaylistDialog(): LiveData<Playlist> = isShowDeletePlaylistDialog

    private val stateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observeState(): LiveData<PlaylistInfoState> = stateLiveData

    private val tracksList = MutableLiveData<List<Track>?>()
    fun observeTracks(): LiveData<List<Track>?> = tracksList
    private val playlistDuration = MutableLiveData<Int>()
    fun observePlaylistDuration(): LiveData<Int> = playlistDuration
    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    private val dateFormatMinSec by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val dateFormatMin by lazy { SimpleDateFormat("mm", Locale.getDefault()) }


    private val openEditPlaylist = SingleLiveEvent<Long>()
    fun observeOpenEditPlaylist(): LiveData<Long> = openEditPlaylist

    private val playlistId: Long? = savedStateHandle[SAVED_STATE_HANDLE_PLAYLIST_ID]

    lateinit var playlistCurrent: Playlist
    lateinit var currentTracks: List<Track>
    fun initPlaylist() {
        viewModelScope.launch {
            playlistId?.let {
                playlistsInteractor.getPlaylist(it).collect { playlist ->
                    playlist?.let { it1 ->
                        renderState(PlaylistInfoState.Content(it1))
                        setTracksDuration(it1)
                        playlistCurrent = playlist
                    }
                }
            }
        }
    }

    fun setTracksDuration(playlist: Playlist){
        if (playlist.trackIds.isNullOrEmpty()){
            tracksList.postValue(null)
            playlistDuration.postValue(0)
        }else{
            viewModelScope.launch {
                playlistsInteractor.getTracksInPlaylist(playlist.trackIds).collect { tracks ->
                    currentTracks = tracks
                    var durationSum = 0L
                    tracks.forEach { track ->
                        durationSum += track.trackTime.toLong()
                    }
                    val durationMin = dateFormatMin.format(durationSum).toInt()
                    playlistDuration.postValue(durationMin)
                    tracksList.postValue(tracks)
                }
            }
        }
    }

    fun onClickTrack(track: Track){

    }

    fun onClickYesDeleteTrack(track: Track){
        viewModelScope.launch {
            playlistsInteractor.deleteTrackInPlaylist(playlistCurrent, track.trackId)
        }
    }
    fun onClickYesDeletePlaylist(playlist: Playlist){


        viewModelScope.launch {
            val result = playlistsInteractor.deletePlaylist(playlist)
            if (result){
                renderState(PlaylistInfoState.Close)
            }
        }
    }

    fun onClickShare(){
        if (playlistCurrent.trackIds.isNullOrEmpty()){
            showMessageLiveData.postValue(resources.getString(R.string.not_tracks_for_shared))
        }else{
            sharingInteractor.shareApp(resources.getString(R.string.playlist), getMessage(playlistCurrent, currentTracks))
        }
        isShowBottomSheetMenu.postValue(false)
    }

    fun getMessage(playlist: Playlist, tracks: List<Track>): String {
        var message = "${playlist.name}\n"
        if (!playlist.description.isNullOrEmpty()){
            message += "${playlist.description}\n"
        }
        message += playlist.numberTracks?.let { "${resources.getQuantityString(R.plurals.tracks, it, it)}\n" }

        tracks.forEachIndexed { index, track ->
            message += "${index + 1}. ${track.artistName} - ${track.trackName} (${dateFormatMinSec.format(track.trackTime.toLong())})\n"
        }
        return message
    }

    fun onClickMore(){
        isShowBottomSheetMenu.postValue(true)
    }

    fun onClickMenuDeletePlaylist(){
        isShowDeletePlaylistDialog.postValue(playlistCurrent)
    }
    fun onClickMenuEditPlaylist(){
        openEditPlaylist.postValue(playlistId!!)
    }

    private fun renderState(state: PlaylistInfoState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SAVED_STATE_HANDLE_PLAYLIST_ID = "playlistId"
    }
}
