package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchTracksRequest
import com.example.playlistmaker.search.data.dto.SearchTracksResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.extension.toDomainModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchTracksRequest(expression))

        return when (response.resultCode) {
            200 -> {
                Resource.Success((response as SearchTracksResponse).results.map {
                    it.toDomainModel()
                })
            }

            else -> {
                Resource.Error(COMMUNICATION_PROBLEMS)
            }
        }
    }

    companion object {
        const val COMMUNICATION_PROBLEMS = "Проблемы со связью"
    }
}