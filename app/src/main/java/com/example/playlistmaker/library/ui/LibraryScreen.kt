package com.example.playlistmaker.library.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.favoriteTracks.ui.FavoriteTracksScreen
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.playlists.ui.PlaylistsScreen
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onTrackClick: (track: Track) -> Unit,
    onPlaylistClick: (playlist: Playlist) -> Unit,
    onClickNewPlaylist: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.medical_library),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    ) { paddingValues ->
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { 2 })
        val selectedTabIndex = pagerState.currentPage
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .padding(horizontal = 16.dp)
                            .height(2.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.favorite_tracks),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.playlists),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> FavoriteTracksScreen(
                        onTrackClick = onTrackClick
                    )

                    1 -> PlaylistsScreen(
                        onPlaylistClick = onPlaylistClick,
                        onClickNewPlaylist = {
                            onClickNewPlaylist()
                        }
                    )
                }
            }
        }
    }
}
