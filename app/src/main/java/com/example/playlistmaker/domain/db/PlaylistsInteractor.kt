package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun createNewPlaylist(playlist: Playlist)

    suspend fun selectAllPlaylists(): Flow<List<Playlist>>

}