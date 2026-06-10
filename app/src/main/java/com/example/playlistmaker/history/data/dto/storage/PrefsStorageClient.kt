package com.example.playlistmaker.history.data.dto.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.history.data.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val prefs: SharedPreferences,
    private val gson: Gson,
    private val type: Type,
) : StorageClient<T> {

    override fun storeData(key: String, data: T) {
        prefs.edit {
            putString(key, gson.toJson(data, type))
        }
    }

    override fun getData(key: String): T? {
        val dataJson = prefs.getString(key, null)
        if (dataJson == null) {
            return null
        } else {
            return gson.fromJson(dataJson, type)
        }
    }

    override fun clearData(key: String) {
        prefs.edit { remove(key) }
    }
}