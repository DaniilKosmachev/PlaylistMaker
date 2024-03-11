package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
): PlaylistsRepository {
    override suspend fun createNewPlaylist(playlistEntity: Playlist) {
        appDatabase.playlistDao().createNewPlaylist(playlistDbConverter.map(playlistEntity))
    }

    override suspend fun selectAllPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlistsEntity = appDatabase.playlistDao().selectAllPlaylists()
            emit(playlistsEntity.map { playlistEntity ->
                playlistDbConverter.map(playlistEntity)
            })
        }
    }

    override suspend fun insertTrackInPlaylist(tracksInPlaylists: TracksInPlaylists) {
        appDatabase.playlistDao().insertTrackInPlaylist(playlistDbConverter.map(tracksInPlaylists))
    }

    override suspend fun updateCountTracksInPlaylist(playlistId: Int) {
        appDatabase.playlistDao().updateTrackCountInPlaylist(playlistId)
    }

    override suspend fun addNewTrackInPlaylistsTransaction(
        tracksInPlaylists: TracksInPlaylists,
        playlistId: Int
    ) {
        appDatabase.playlistDao().addTrackInPlaylistTransaction(playlistDbConverter.map(tracksInPlaylists), playlistId)
    }

    override suspend fun checkTrackInPlaylist(
        trackId: Int
    ): Flow<List<Int>>  {
        return flow {
            val playlistIds = appDatabase.playlistDao().checkTrackInPlaylists(trackId)
            emit(playlistIds)
        }
    }

    override suspend fun selectAllTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.playlistDao().selectAllTracksInPlaylist(playlistId)
            emit(tracks.map {
                tracksInPlaylistsEntity -> playlistDbConverter.mapOnTrack(tracksInPlaylistsEntity)
            })
        }
    }

    override suspend fun removeTrackFromPlaylistTransaction(trackId: Int, playlistId: Int) {
        appDatabase.playlistDao().deleteTrackFromDbTransaction(trackId, playlistId)
    }

    override suspend fun removePlaylistFromDb(playlistId: Int) {
        appDatabase.playlistDao().deletePlaylistTransaction(playlistId)
    }

    override suspend fun editPlaylistInfo(
        playlistId: Int,
        name: String,
        description: String?,
        imageUri: String?
    ) {
        appDatabase.playlistDao().updatePlaylistInfo(playlistId, name, description, imageUri)
    }
}