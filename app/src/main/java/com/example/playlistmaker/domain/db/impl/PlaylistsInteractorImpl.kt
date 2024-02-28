package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistsInteractorImpl (
    private val repository: PlaylistsRepository
): PlaylistsInteractor {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        repository.createNewPlaylist(playlist)
    }

}