package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.TracksInPlaylists

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = null,
            name = playlist.name,
            uri = playlist.uri,
            description = playlist.description,
            tracksId = playlist.tracksId,
            count = playlist.count
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            uri = playlistEntity.uri,
            tracksId = playlistEntity.tracksId,
            count = playlistEntity.count
        )
    }

    fun map(tracksInPlaylists: TracksInPlaylists): TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(
            id = null,
            trackId = tracksInPlaylists.trackId,
            playlistId = tracksInPlaylists.playlistId
        )
    }

    fun map(tracksInPlaylists: TracksInPlaylistsEntity): TracksInPlaylists {
        return TracksInPlaylists(
            id = tracksInPlaylists.id,
            trackId = tracksInPlaylists.trackId,
            playlistId = tracksInPlaylists.playlistId
        )
    }
}