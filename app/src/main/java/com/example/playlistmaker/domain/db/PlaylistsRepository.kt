package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createNewPlaylist(playlistEntity: Playlist)

    suspend fun selectAllPlaylists(): Flow<List<Playlist>>
}