package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import java.util.function.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)
}