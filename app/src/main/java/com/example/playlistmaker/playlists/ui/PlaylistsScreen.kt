package com.example.playlistmaker.playlists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.LibraryMessage
import com.example.playlistmaker.main.ui.MainButton
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel = koinViewModel(),
    onPlaylistClick: (playlist: Playlist) -> Unit,
    onClickNewPlaylist: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        PlaylistsState.Empty -> Message { onClickNewPlaylist() }
        is PlaylistsState.Content -> Playlists(
            (uiState.value as PlaylistsState.Content).playlists,
            onPlaylistClick,
            onClickNewPlaylist
        )
    }
}

@Composable
fun Message(onClickNewPlaylist: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        MainButton(R.string.new_playlist) {
            onClickNewPlaylist()
        }
        Spacer(modifier = Modifier.height(46.dp))
        LibraryMessage(R.string.no_playlists)
    }
}

@Composable
fun Playlists(
    playlists: List<Playlist>,
    onPlaylistClick: (playlist: Playlist) -> Unit,
    onClickNewPlaylist: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        MainButton(R.string.new_playlist) {
            onClickNewPlaylist()
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = playlists, key = { it.id }) { playlist ->
                PlaylistItem(
                    playlist = playlist,
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        }
    }
}
