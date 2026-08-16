package com.example.playlistmaker.playlistCreate.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val pathImageCover: String?,
    val trackIds: String?,
    val numberTracks: Int?,
    val created: Long,
)
