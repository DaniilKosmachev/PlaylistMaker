package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createNewPlaylist(playlistEntity: Playlist)

    suspend fun selectAllPlaylists(): Flow<List<Playlist>>

    suspend fun insertTrackInPlaylist(tracksInPlaylists: TracksInPlaylists)

    suspend fun updateCountTracksInPlaylist(playlistId: Int)
}