package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl (
    private val repository: PlaylistsRepository
): PlaylistsInteractor {
    override suspend fun createNewPlaylist(playlist: Playlist) {
        repository.createNewPlaylist(playlist)
    }

    override suspend fun selectAllPlaylists(): Flow<List<Playlist>> {
        return repository.selectAllPlaylists()
    }

    override suspend fun insertTrackInPlaylist(tracksInPlaylists: TracksInPlaylists) {
        repository.insertTrackInPlaylist(tracksInPlaylists)
    }

    override suspend fun updateCountTracksInPlaylist(playlistId: Int) {
        repository.updateCountTracksInPlaylist(playlistId)
    }

    override suspend fun addNewTrackInPlaylistsTransaction(
        tracksInPlaylists: TracksInPlaylists,
        playlistId: Int
    ) {
        repository.addNewTrackInPlaylistsTransaction(tracksInPlaylists, playlistId)
    }

    override suspend fun checkTrackInPlaylist(
        trackId: Int
    ): Flow<List<Int>> {
        return repository.checkTrackInPlaylist(trackId)
    }

    override suspend fun selectAllTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
        return repository.selectAllTracksInPlaylist(playlistId)
    }

    override suspend fun removeTrackFromPlaylistTransaction(trackId: Int, playlistId: Int) {
        repository.removeTrackFromPlaylistTransaction(trackId, playlistId)
    }

    override suspend fun removePlaylistFromDb(playlistId: Int) {
        repository.removePlaylistFromDb(playlistId)
    }

    override suspend fun editPlaylistInfo(
        playlistId: Int,
        name: String,
        description: String?,
        imageUri: String?
    ) {
        repository.editPlaylistInfo(playlistId, name, description, imageUri)
    }

    override suspend fun updatePlaylistInfo(playlistId: Int): Flow<Playlist> {
        return repository.updatePlaylistInfo(playlistId)
    }

}