package com.example.playlistmaker.main.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

// Set of Material typography styles to start with
val AppTypography = Typography(
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
    ),

    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 11.sp,
    ),


    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 19.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
    ),


    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)