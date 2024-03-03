package com.example.playlistmaker.domain.player.model

sealed interface TracksInPlaylistsState {
    data class inPlaylist(
        var data: List<TracksInPlaylists>
    ): TracksInPlaylistsState

    object notInPlaylist: TracksInPlaylistsState

}