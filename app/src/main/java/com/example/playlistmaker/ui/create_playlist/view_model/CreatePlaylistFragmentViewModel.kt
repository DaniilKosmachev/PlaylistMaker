package com.example.playlistmaker.ui.create_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class CreatePlaylistFragmentViewModel(
    private var playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    open var dbJob: Job? = null

        fun addNewPlaylist(playlist: Playlist) {
            dbJob = viewModelScope.launch(Dispatchers.IO) {playlistsInteractor.createNewPlaylist(playlist)}
        }
}