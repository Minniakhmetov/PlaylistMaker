package com.example.playlistmaker.history.data.dto.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.history.data.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val dataKey: String,
    private val type: Type,
) : StorageClient<T> {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun storeData(data: T) {
        prefs.edit {
            putString(dataKey, gson.toJson(data, type))
        }
    }

    override fun getData(): T? {
        val dataJson = prefs.getString(dataKey, null)
        if (dataJson == null) {
            return null
        } else {
            return gson.fromJson(dataJson, type)
        }
    }

    override fun clearData() {
        prefs.edit { remove(SEARCH_HISTORY_KEY) }
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
    }
}