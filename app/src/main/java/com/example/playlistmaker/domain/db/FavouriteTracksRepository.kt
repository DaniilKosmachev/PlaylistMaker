package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun addTrackInDbFavourite(trackEntity: Track)

    fun deleteTrackInDbFavourite(trackEntity: Track)

    fun selectAllTracksInDbFavourite(): Flow<List<Track>>
}