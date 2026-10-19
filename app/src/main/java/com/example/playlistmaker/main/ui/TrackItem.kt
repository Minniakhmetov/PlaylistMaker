package com.example.playlistmaker.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TrackItem(
    track: Track,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .build(),
            placeholder = painterResource(R.drawable.ic_track_placeholder_45),
            error = painterResource(R.drawable.ic_track_placeholder_45),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 13.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.trackName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.artistName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

                Icon(
                    painter = painterResource(R.drawable.ic_track_separator_3),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = formatDuration(track.trackTime),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Icon(
            modifier = Modifier.padding(start = 8.dp, end = 12.dp, top = 18.dp, bottom = 19.dp),
            painter = painterResource(R.drawable.ic_user_agreement_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatDuration(trackTimeMillis: String): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())

}
