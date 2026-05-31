package com.example.playlistmaker.history.data

interface StorageClient<T> {
    fun storeData(key: String, data: T)
    fun getData(key: String): T?
    fun clearData(key: String)
}