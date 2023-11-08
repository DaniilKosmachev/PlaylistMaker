package com.example.playlistmaker.domain.api.media_player

import com.example.playlistmaker.domain.models.PlayerParams
import com.example.playlistmaker.domain.models.Track

interface PlayerInteractor {
    fun prepareMediaPlayer(track: Track)
    fun startPreviewTrack()
    fun pausePreviewTrack()
    fun changePlaybackProgress(): PlayerParams
    fun destroyPlayer()
}