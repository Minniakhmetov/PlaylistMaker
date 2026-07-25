package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
//    fun searchTracks(expression: String, consumer: TracksConsumer)
//
//    interface TracksConsumer {
//        fun consume(foundTracks: List<Track>?, errorMessage: String?)
//    }
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}