package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist

interface PlaylistsRepository {

    suspend fun createNewPlaylist(playlistEntity: Playlist)

}