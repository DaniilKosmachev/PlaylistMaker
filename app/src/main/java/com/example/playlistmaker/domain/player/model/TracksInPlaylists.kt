package com.example.playlistmaker.domain.player.model

import com.example.playlistmaker.domain.search.model.Track

data class TracksInPlaylists(
    val id: Int?,
    val trackId: Int,
    val track: Track,
    val playlistId: Int
)
