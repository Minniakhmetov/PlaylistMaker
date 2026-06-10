package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.SearchTracksApi
import com.example.playlistmaker.search.di.SearchConstants.BASE_URL
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {

    single<SearchTracksApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTracksApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

}
