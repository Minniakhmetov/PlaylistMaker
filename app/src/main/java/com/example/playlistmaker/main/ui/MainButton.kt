package com.example.playlistmaker.main.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MainButton(
    labelId: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.height(36.dp),
        onClick = { onClick() },
        content = {
            Text(
                text = stringResource(labelId),
                style = MaterialTheme.typography.labelSmall
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface
        )
    )
}
