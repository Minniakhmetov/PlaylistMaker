package com.example.playlistmaker.data.sharedPrefs

import android.content.Context
import androidx.core.content.edit

class SettingsPrefsManager(context: Context) : SettingPrefsClient {
    private val sharedPreferences =
        context.getSharedPreferences(SETTING_PREFERENCES, Context.MODE_PRIVATE)

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

    companion object {
        const val SETTING_PREFERENCES = "setting_preferences"
    }
}