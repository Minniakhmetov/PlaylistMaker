package com.example.playlistmaker.favoriteTracks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.LibraryMessage
import com.example.playlistmaker.main.ui.TrackItem
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksScreen(
    viewModel: FavoriteTracksViewModel = koinViewModel(),
    onTrackClick: (track: Track) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is FavoriteTracksState.Empty -> FavoriteTracksMessage()
        is FavoriteTracksState.Content -> FavoriteTracks(
            (uiState.value as FavoriteTracksState.Content).favoriteTracks,
            onTrackClick
        )
    }
}

@Composable
fun FavoriteTracksMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(106.dp))
        LibraryMessage(R.string.library_is_empty)
    }
}

@Composable
fun FavoriteTracks(
    favoriteTracks: List<Track>,
    onTrackClick: (track: Track) -> Unit
) {
    Column() {
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = favoriteTracks, key = { it.trackId }) { track ->
                TrackItem(
                    track = track,
                    onClick = { onTrackClick(track) })
            }
        }
    }
}
