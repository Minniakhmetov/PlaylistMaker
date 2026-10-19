package com.example.playlistmaker.library.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun LibraryMessage(labelId: Int) {
    Image(
        painter = painterResource(R.drawable.ic_nothing_was_found_120),
        contentDescription = null
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(labelId),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelMedium
    )
}
