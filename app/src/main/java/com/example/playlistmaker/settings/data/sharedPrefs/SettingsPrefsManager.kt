package com.example.playlistmaker.settings.data.sharedPrefs

import android.content.SharedPreferences
import android.util.Log
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

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    override fun getInt(key: String): Int {
        val id = sharedPreferences.getInt(key, -1)
        return id
    }
}