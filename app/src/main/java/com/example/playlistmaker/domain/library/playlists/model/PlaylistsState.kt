package com.example.playlistmaker.domain.library.playlists.model

sealed interface PlaylistsState {
    data class Content(
        val playlists: List<Playlist>
    ): PlaylistsState

    object Empty: PlaylistsState
}