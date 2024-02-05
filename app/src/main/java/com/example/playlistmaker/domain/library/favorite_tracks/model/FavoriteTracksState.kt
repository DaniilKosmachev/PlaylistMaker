package com.example.playlistmaker.domain.library.favorite_tracks.model

import com.example.playlistmaker.domain.search.model.Track

sealed interface FavoriteTracksState {

    data class Content(
        val tracks: List<Track>
    ): FavoriteTracksState

    object Empty: FavoriteTracksState
}