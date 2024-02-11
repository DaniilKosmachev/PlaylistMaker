package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {

    suspend fun addTrackInDbFavourite(track: Track)

    suspend fun deleteTrackInDbFavourite(track: Track)

    suspend fun selectAllTracksInDbFavourite(): Flow<List<Track>>

}