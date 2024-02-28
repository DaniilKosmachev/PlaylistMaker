package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist

interface PlaylistsInteractor {

    suspend fun createNewPlaylist(playlist: Playlist)

}