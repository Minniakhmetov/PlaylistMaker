package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchTracksRequest
import com.example.playlistmaker.search.data.dto.SearchTracksResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.extension.toDomainModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchTracksRequest(expression))

        when (response.resultCode) {
            200 -> {
                emit(Resource.Success((response as SearchTracksResponse).results.map {
                    it.toDomainModel()
                }))
            }

            else -> {
                emit(Resource.Error(COMMUNICATION_PROBLEMS))
            }
        }
    }

    companion object {
        const val COMMUNICATION_PROBLEMS = "Проблемы со связью"
    }
}