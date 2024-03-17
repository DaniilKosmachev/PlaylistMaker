package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    var playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var _StatusPlaylist = MutableLiveData<PlaylistsState>()

    fun getStatusAllPlaylists(): LiveData<PlaylistsState> = _StatusPlaylist

    fun updateListPlaylistsFromDb() {
       viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .selectAllPlaylists()
                .collect {
                    playlists ->
                    when(playlists.isEmpty()) {
                        true -> _StatusPlaylist.postValue(PlaylistsState.Empty)
                        false -> _StatusPlaylist.postValue(PlaylistsState.Content(playlists))
                    }
                }
        }
    }

}