package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val repository: FavouriteTracksRepository
): FavouriteTracksInteractor {

    override suspend fun addTrackInDbFavourite(track: Track) {
        repository.addTrackInDbFavourite(track)
    }

    override suspend fun deleteTrackInDbFavourite(track: Track) {
        repository.deleteTrackInDbFavourite(track)
    }

    override suspend fun selectAllTracksInDbFavourite(): Flow<List<Track>> {
        return repository.selectAllTracksInDbFavourite()
    }
}