package com.example.playlistmaker.ui.edit_playlist.view_model

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.ui.create_playlist.view_model.CreatePlaylistFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private var playlistsInteractor: PlaylistsInteractor
): CreatePlaylistFragmentViewModel(playlistsInteractor) {

    override var dbJob: Job? = null

    fun editNewPlaylist(playlistId: Int, name: String, description: String?, imageUri: String?) {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.editPlaylistInfo(playlistId, name, description, imageUri)
        }
    }
}