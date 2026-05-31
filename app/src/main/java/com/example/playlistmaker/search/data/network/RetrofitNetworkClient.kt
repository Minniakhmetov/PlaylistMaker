package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchTracksRequest
import java.io.IOException


class RetrofitNetworkClient(private val tracksService: SearchTracksApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is SearchTracksRequest) {
            try {
                val resp = tracksService.getTracks(dto.expression).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } catch (e: IOException) {
                return Response().apply { resultCode = 400 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
