package com.example.playlistmaker.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.TrackItem
import com.example.playlistmaker.main.ui.MainButton
import com.example.playlistmaker.search.domain.models.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClick: (track: Track) -> Unit,
    onClickClearHistory: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.search),
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
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                val state = rememberTextFieldState()
                val interactionSource = remember { MutableInteractionSource() }
                val isFocused by interactionSource.collectIsFocusedAsState()
                val focusManager = LocalFocusManager.current

                LaunchedEffect(state) {
                    snapshotFlow { state.text.toString() }
                        .collect {
                            if (state.text.isEmpty()) {
                                viewModel.removeLatestSearchText()
                                viewModel.searchDebounce(it)
                            }
                            if (isFocused) {
                                viewModel.searchDebounce(it)
                            }
                        }
                }
                LaunchedEffect(isFocused) {
                    if (isFocused) {
                        if (state.text.isEmpty()) {
                            viewModel.searchDebounce("")
                        }
                    } else {
                        if (state.text.isEmpty()) {
                            viewModel.renderStateStart()
                        }
                    }
                }
                OutlinedTextField(
                    state = state,
                    contentPadding = PaddingValues(vertical = 5.dp, horizontal = 0.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text(stringResource(R.string.search)) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = colorResource(R.color.yp_black),
                        unfocusedTextColor = colorResource(R.color.yp_black),
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_16),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (state.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    state.edit { delete(0, length) }
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_clear_16),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    },
                    interactionSource = interactionSource,
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }

            val uiState = viewModel.uiState.collectAsState()
            when (uiState.value) {
                SearchState.Start -> {}
                SearchState.Loading -> ProgressBar()
                is SearchState.Content -> SearchTracks(
                    (uiState.value as SearchState.Content).tracks,
                    onTrackClick
                )

                is SearchState.ContentHistory -> SearchHistory(
                    (uiState.value as SearchState.ContentHistory).tracks,
                    onTrackClick,
                    onClickClearHistory
                )

                is SearchState.Empty -> Placeholder(
                    R.string.nothing_was_found,
                    R.drawable.ic_nothing_was_found_120
                )

                is SearchState.Error -> Placeholder(
                    R.string.communication_problems,
                    R.drawable.ic_communication_problems_120
                ) {
                    viewModel.repeatLastSearch()
                }
            }
        }
    }
}

@Composable
fun Placeholder(
    messageId: Int,
    imageId: Int,
    onClickUpdate: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 102.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(messageId),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        if (onClickUpdate != null) {
            Spacer(modifier = Modifier.height(24.dp))
            MainButton(R.string.update) {
                onClickUpdate()
            }
        }
    }
}

@Composable
fun ProgressBar() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 148.dp)
                .size(44.dp),
            color = colorResource(R.color.yp_blue),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun SearchTracks(
    tracks: List<Track>,
    onTrackClick: (track: Track) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        items(items = tracks, key = { it.trackId }) { track ->
            TrackItem(
                track = track,
                onClick = { onTrackClick(track) })
        }
    }
}

@Composable
fun SearchHistory(
    tracks: List<Track>,
    onTrackClick: (track: Track) -> Unit,
    onClickClearHistory: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.padding(top = 18.dp, end = 12.dp),
            text = stringResource(R.string.you_searched),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            items(items = tracks, key = { it.trackId }) { track ->
                TrackItem(
                    track = track,
                    onClick = { onTrackClick(track) })
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    MainButton(
                        R.string.clear_history,
                        onClick = {
                            onClickClearHistory()
                        })
                }
            }
        }
    }
}
