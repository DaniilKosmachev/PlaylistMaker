package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.search.model.Track

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