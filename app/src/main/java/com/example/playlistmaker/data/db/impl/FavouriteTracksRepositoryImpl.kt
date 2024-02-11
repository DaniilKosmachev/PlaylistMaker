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


    override suspend fun addTrackInDbFavourite(track: Track) {
            appDatabase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrackInDbFavourite(track: Track) {
            appDatabase.trackDao().deleteTrack(trackDbConverter.map(track))
    }

    override suspend fun selectAllTracksInDbFavourite(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().selectAllTracks()
        emit(tracks.map { trackEntity ->
            trackDbConverter.map(trackEntity) }
        )
    }
}