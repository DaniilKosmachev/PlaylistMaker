package com.example.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private var playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var _StatusTracksInPlaylist = MutableLiveData<FavoriteTracksState>()

    fun statusTracksInPlaylist(): LiveData<FavoriteTracksState> {
        return _StatusTracksInPlaylist
    }

    var dbJob: Job? = null

    fun selectAllTrackInPlaylist(playlistId: Int) {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .selectAllTracksInPlaylist(playlistId)
                .collect {
                    tracks ->
                    if (tracks.isNullOrEmpty()) _StatusTracksInPlaylist.postValue(FavoriteTracksState.Empty)
                    else _StatusTracksInPlaylist.postValue(FavoriteTracksState.Content(tracks))
                }
        }
    }

    fun deleteTrackFromDb(trackId: Int, playlistId: Int) {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.removeTrackFromPlaylistTransaction(trackId, playlistId)
            selectAllTrackInPlaylist(playlistId)
        }
    }
}