package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import java.util.function.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TrackSearchResponceParams>)
}