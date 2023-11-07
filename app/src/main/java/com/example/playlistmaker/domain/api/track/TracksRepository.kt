package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}