package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams

interface TracksRepository {
    fun searchTracks(expression: String): TrackSearchResponceParams
}