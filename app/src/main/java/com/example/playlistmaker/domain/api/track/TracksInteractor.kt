package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.TrackSearchResponceParams
import java.util.function.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TrackSearchResponceParams>)
}