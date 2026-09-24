package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchTracksRequest
import java.io.IOException


class RetrofitNetworkClient(private val tracksService: SearchTracksApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is SearchTracksRequest) {
            try {
                val resp = tracksService.getTracks(dto.expression)
                resp.apply {
                    resultCode = 200
                }
            } catch (e: IOException) {
                Response().apply { resultCode = 400 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}
