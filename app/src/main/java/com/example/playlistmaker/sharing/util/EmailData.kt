package com.example.playlistmaker.sharing.util

data class EmailData(
    val addresses: Array<String>,
    val data: String,
    val subject: String,
    val supportText: String,
)
