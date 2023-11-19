package com.example.playlistmaker.data.player

import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.search.model.Track

interface PlayerRepository {
    fun prepareMediaPlayer(track: Track)
    fun startPreviewTrack()
    fun pausePreviewTrack()
    fun changePlaybackProgress(): PlayerParams
    fun destroyPlayer()
}