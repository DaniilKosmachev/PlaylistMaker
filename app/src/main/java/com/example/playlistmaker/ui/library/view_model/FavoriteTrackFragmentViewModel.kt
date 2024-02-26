package com.example.playlistmaker.ui.library.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteTrackFragmentViewModel(
    var favouriteTracksInteractor: FavouriteTracksInteractor
): ViewModel() {

    private var dbJob: Job? = null

    private var mutableStatusFavoriteTracks = MutableLiveData<FavoriteTracksState>()

    fun getStatusFavoriteTracks(): LiveData<FavoriteTracksState> = mutableStatusFavoriteTracks


    fun updateListFromDb() {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            favouriteTracksInteractor
                .selectAllTracksInDbFavourite()
                .collect { tracks ->
                    when (tracks.isEmpty()) {
                        true -> {
                            mutableStatusFavoriteTracks.postValue(FavoriteTracksState.Empty)
                        }

                        false -> {
                            tracks.map { track ->
                                track.isFavorite = true
                            }
                            mutableStatusFavoriteTracks.postValue(FavoriteTracksState.Content(tracks))
                        }
                    }
                }
            Log.d("TEST", "В методе ViewModel получили треки")
        }
    }
}