package com.example.playlistmaker.main.ui.utils

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R

object TextManager {
    fun getCorrectEndingTextTrack(context: Context, number: Int?): String {
        return when (number) {
            null, 0 -> {
                getString(context,R.string.text_tracks_null)
            }
            11, 12, 13, 14 -> {
                "$number ${getString(context,R.string.text_tracks_2_4)}"
            }
            else -> {
                when (number % 10) {
                    1 -> "$number ${getString(context,R.string.text_tracks_1)}"
                    2, 3, 4 -> "$number ${getString(context,R.string.text_tracks_2_4)}"
                    else -> "$number ${getString(context,R.string.text_tracks_other)}"
                }
            }
        }
    }
}