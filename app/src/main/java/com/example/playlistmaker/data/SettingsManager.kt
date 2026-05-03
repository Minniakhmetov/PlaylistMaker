package com.example.playlistmaker.data

import android.content.Context
import androidx.core.content.edit


class SettingsManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SETTING_PREFERENCES, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    companion object {
        const val SETTING_PREFERENCES = "setting_preferences"
    }
}
