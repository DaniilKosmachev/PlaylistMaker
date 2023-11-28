package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import java.util.concurrent.ExecutorService
import java.util.function.Consumer

class TracksInteractorImpl(private val repository: TracksRepository, private val executors: ExecutorService) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: Consumer<TrackSearchResponceParams>) {
        executors.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}