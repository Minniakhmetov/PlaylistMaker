package com.example.playlistmaker.data.dto

class SearchTracksResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>,
) : Response()
