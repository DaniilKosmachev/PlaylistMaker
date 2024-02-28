package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.library.playlists.model.Playlist

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
}