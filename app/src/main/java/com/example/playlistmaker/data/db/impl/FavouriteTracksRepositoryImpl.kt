package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.domain.db.FavouriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): FavouriteTracksRepository {

    override fun addTrackInDbFavourite(track: Track) {
        TODO("Not yet implemented")
    }

    override fun deleteTrackInDbFavourite(trackEntity: Track) {
        TODO("Not yet implemented")
    }

    override fun selectAllTracksInDbFavourite(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().selectAllTracks()
        emit(tracks.map { trackEntity ->
            trackDbConverter.map(trackEntity) }
        )
    }
}