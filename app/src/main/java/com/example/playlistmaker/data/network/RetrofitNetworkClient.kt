package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SearchTracksRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitNetworkClient : NetworkClient {
    private val tracksRetrofit = Retrofit.Builder()
        .baseUrl(TRACKS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val tracksService = tracksRetrofit.create(SearchTracksApi::class.java)

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

    companion object {
        const val TRACKS_BASE_URL = "https://itunes.apple.com"
    }
}
