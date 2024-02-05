package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteTrackFragmentViewModel(
    var favouriteTracksInteractor: FavouriteTracksInteractor
): ViewModel() {

    private var dbJob: Job? = null

    init {
        dbJob = viewModelScope.launch {
            favouriteTracksInteractor
                .selectAllTracksInDbFavourite()
                .collect { tracks ->
                    when(tracks.isEmpty()) {
                        true -> {
                            mutableStatusFavoriteTracks.postValue(FavoriteTracksState.Empty)
                        }
                        false -> {
                            mutableStatusFavoriteTracks.postValue(FavoriteTracksState.Content(tracks))
                        }
                    }
                }
        }
    }

    private var mutableStatusFavoriteTracks = MutableLiveData<FavoriteTracksState>()

    fun getStatusFavoriteTracks(): LiveData<FavoriteTracksState> = mutableStatusFavoriteTracks



}