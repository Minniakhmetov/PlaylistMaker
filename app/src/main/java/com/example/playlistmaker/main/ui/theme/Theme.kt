package com.example.playlistmaker.main.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    primary = YpBlue,
    onPrimary = YpWhite,
    background = YpBlack,
    onBackground = YpWhite,
    surface = YpBlack,
    onSurface = YpWhite,
    surfaceVariant = YpBlack,
    onSurfaceVariant = YpWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = YpBlue,
    onPrimary = YpBlack,
    background = YpWhite,
    onBackground = YpLightGray,
    surface = YpWhite,
    onSurface = YpBlack,
    surfaceVariant = YpTextGray,
    onSurfaceVariant = YpTextGray,
)

@Composable
fun FirstComposeProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}