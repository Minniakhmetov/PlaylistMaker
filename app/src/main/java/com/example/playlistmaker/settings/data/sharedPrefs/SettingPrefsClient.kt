package com.example.playlistmaker.settings.data.sharedPrefs


interface SettingPrefsClient {
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
}
