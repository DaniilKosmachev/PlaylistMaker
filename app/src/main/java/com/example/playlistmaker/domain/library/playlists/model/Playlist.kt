package com.example.playlistmaker.domain.library.playlists.model

data class Playlist(
    val id: Int?,
    val name: String,
    val description: String?,
    val uri: String?,
    val tracksId: String?,
    val count: Int?
)
