package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
): PlaylistsRepository {
    override suspend fun createNewPlaylist(playlistEntity: Playlist) {
        appDatabase.playlistDao().createNewPlaylist(playlistDbConverter.map(playlistEntity))
    }
}