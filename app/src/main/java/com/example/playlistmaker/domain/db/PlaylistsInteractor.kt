package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun createNewPlaylist(playlist: Playlist)

    suspend fun selectAllPlaylists(): Flow<List<Playlist>>

    suspend fun insertTrackInPlaylist(tracksInPlaylists: TracksInPlaylists)

    suspend fun updateCountTracksInPlaylist(playlistId: Int)

    suspend fun addNewTrackInPlaylistsTransaction(tracksInPlaylists: TracksInPlaylists, playlistId: Int)

    suspend fun checkTrackInPlaylist(trackId: Int): Flow<List<Int>>

    suspend fun selectAllTracksInPlaylist(playlistId: Int): Flow<List<Track>>

}