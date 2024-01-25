package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<TrackSearchResponceParams>
}