package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchTracksRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class RetrofitNetworkClient(private val tracksService: SearchTracksApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is SearchTracksRequest) {
            return withContext(Dispatchers.IO){
                try {
                    val resp = tracksService.getTracks(dto.expression)
                    resp.apply {
                        resultCode = 200
                    }
                } catch (e: IOException){
                    Response().apply { resultCode = 400 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
