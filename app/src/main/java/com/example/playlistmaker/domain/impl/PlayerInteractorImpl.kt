package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.media_player.PlayerInteractor
import com.example.playlistmaker.domain.api.media_player.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerParams
import com.example.playlistmaker.domain.models.Track

class PlayerInteractorImpl(private var repository: PlayerRepository): PlayerInteractor {

    override fun prepareMediaPlayer(track: Track) {
        repository.prepareMediaPlayer(track)
    }

    override fun startPreviewTrack() {
        repository.startPreviewTrack()
    }

    override fun pausePreviewTrack() {
        repository.pausePreviewTrack()
    }

    override fun changePlaybackProgress(): PlayerParams {
        return repository.changePlaybackProgress()
    }


    override fun destroyPlayer() {
        repository.destroyPlayer()
    }

}