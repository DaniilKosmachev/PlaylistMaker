package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {

    fun addTrackInDbFavourite(track: Track)

    fun deleteTrackInDbFavourite(track: Track)

    fun selectAllTracksInDbFavourite(): Flow<List<Track>>

}