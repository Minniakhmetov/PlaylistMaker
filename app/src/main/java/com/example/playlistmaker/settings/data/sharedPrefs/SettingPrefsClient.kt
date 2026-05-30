package com.example.playlistmaker.settings.data.sharedPrefs


interface SettingPrefsClient {
    fun putString(key: String, value: String)
    fun getString(key: String): String
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
}
