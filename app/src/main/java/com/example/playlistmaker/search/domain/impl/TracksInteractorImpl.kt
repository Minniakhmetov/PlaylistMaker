package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository,
) : TracksInteractor {

    //    private val executor = Executors.newCachedThreadPool()
//
//    override fun searchTracks(
//        expression: String,
//        consumer: TracksInteractor.TracksConsumer,
//    ) {
//        executor.execute {
//            when (val resource = repository.searchTracks(expression)) {
//                is Resource.Success -> {
//                    consumer.consume(resource.data, null)
//                }
//
//                is Resource.Error -> {
//                    consumer.consume(null, resource.message)
//                }
//            }
//        }
//    }
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result->
            when(result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }

            }
        }
    }

}
