package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>, ResponceStatus>> {
            return repository.searchTracks(expression).map { result ->
                        Pair(result.tracks, result.resultResponse)
                    }
                }
}