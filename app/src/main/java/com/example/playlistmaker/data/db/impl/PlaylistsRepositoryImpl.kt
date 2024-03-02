package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
): PlaylistsRepository {
    override suspend fun createNewPlaylist(playlistEntity: Playlist) {
        appDatabase.playlistDao().createNewPlaylist(playlistDbConverter.map(playlistEntity))
    }

    override suspend fun selectAllPlaylists(): Flow<List<Playlist>> = flow {
       val playlistsEntity = appDatabase.playlistDao().selectAllPlaylists()
        emit( playlistsEntity.map { playlistEntity ->
            playlistDbConverter.map(playlistEntity)
        })
    }

    override suspend fun insertTrackInPlaylist(tracksInPlaylists: TracksInPlaylists) {
        appDatabase.playlistDao().insertTrackInPlaylist(playlistDbConverter.map(tracksInPlaylists))
    }

    override suspend fun updateCountTracksInPlaylist(playlistId: Int) {
        appDatabase.playlistDao().updateTrackCountInPlaylist(playlistId)
    }
}