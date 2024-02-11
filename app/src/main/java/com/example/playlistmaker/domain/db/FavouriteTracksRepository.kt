package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    suspend fun addTrackInDbFavourite(trackEntity: Track)

    suspend fun deleteTrackInDbFavourite(trackEntity: Track)

    suspend fun selectAllTracksInDbFavourite(): Flow<List<Track>>
}