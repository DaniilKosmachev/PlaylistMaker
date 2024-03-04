package com.example.playlistmaker.domain.player.model

sealed interface TracksInPlaylistsState {
    data class tracksInPlaylist(
        val data: List<TracksInPlaylists>
    ): TracksInPlaylistsState
    object noTracksInPlaylist: TracksInPlaylistsState
}