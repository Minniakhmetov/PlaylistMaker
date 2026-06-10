package com.example.playlistmaker.settings.data.sharedPrefs

import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsPrefsManager(
    private val sharedPreferences: SharedPreferences,
) : SettingPrefsClient {

    override fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}