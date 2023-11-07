package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.track.TracksInteractor
import com.example.playlistmaker.domain.api.track.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors
import java.util.function.Consumer

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}