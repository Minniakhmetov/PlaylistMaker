package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SearchTracksRequest
import com.example.playlistmaker.data.dto.SearchTracksResponse
import com.example.playlistmaker.data.extension.toDomainModel
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track>? {
        val response = networkClient.doRequest(SearchTracksRequest(expression))
        return when (response.resultCode) {
            200 -> {
                (response as SearchTracksResponse).results.map {
                    it.toDomainModel()
                }
            }

            else -> {
                null
            }
        }
    }
}
