package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.db.FavouriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(private val repository: FavouriteTracksRepository): FavouriteTracksInteractor {

    override fun addTrackInDbFavourite(track: Track) {
        TODO("Not yet implemented")
    }

    override fun deleteTrackInDbFavourite(track: Track) {
        TODO("Not yet implemented")
    }

    override fun selectAllTracksInDbFavourite(): Flow<List<Track>> {
        return repository.selectAllTracksInDbFavourite()
    }
}