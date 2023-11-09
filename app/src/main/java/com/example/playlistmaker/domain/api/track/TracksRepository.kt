package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.TrackSearchResponceParams

interface TracksRepository {
    fun searchTracks(expression: String): TrackSearchResponceParams
}