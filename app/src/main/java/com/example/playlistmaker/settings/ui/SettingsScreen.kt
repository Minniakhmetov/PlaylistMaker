package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.theme.YpBlue
import com.example.playlistmaker.main.ui.theme.YpBlue48
import com.example.playlistmaker.main.ui.theme.YpLightGray
import com.example.playlistmaker.main.ui.theme.YpTextGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickOpenLink: () -> Unit,
    onClickShareApp: () -> Unit,
    onClickOpenSupport: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text(
                        text = stringResource(R.string.settings),
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
            Spacer(modifier = Modifier.height(24.dp))
            SettingButton(
                R.string.dark_theme,
                isSwitch = true,
                checked = checked,
                onCheckedChange = onCheckedChange,
                onClick = {}
            )
            SettingButton(
                R.string.share_the_app,
                R.drawable.ic_share_the_app_24,
                onClick = onClickShareApp
            )
            SettingButton(
                R.string.write_to_support,
                R.drawable.ic_write_to_support_24,
                onClick = onClickOpenSupport
            )
            SettingButton(
                R.string.user_agreement,
                R.drawable.ic_user_agreement_24,
                onClick = onClickOpenLink
            )
        }
    }
}

@Composable
private fun SettingButton(
    titleId: Int,
    iconId: Int? = null,
    isSwitch: Boolean? = null,
    onClick: () -> Unit,
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable { onClick() }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(titleId),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (iconId != null) Icon(
            painter = painterResource(iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        isSwitch?.let {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = YpBlue,
                    checkedTrackColor = YpBlue48,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = YpTextGray,
                    uncheckedTrackColor = YpLightGray,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}
