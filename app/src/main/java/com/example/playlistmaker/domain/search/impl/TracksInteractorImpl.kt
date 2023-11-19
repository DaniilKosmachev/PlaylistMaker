package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import java.util.concurrent.Executors
import java.util.function.Consumer

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: Consumer<TrackSearchResponceParams>) {
        executor.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}