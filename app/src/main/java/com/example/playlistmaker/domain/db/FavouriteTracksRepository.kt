package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun addTrackInDbFavourite(trackEntity: TrackEntity)

    fun deleteTrackInDbFavourite(trackEntity: TrackEntity)

    fun selectAllTracksInDbFavourite(): Flow<List<Track>>
}