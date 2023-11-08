package com.example.playlistmaker.data.network

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerParams
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track

class PlayerRepositoryImpl: PlayerRepository {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerState: PlayerState = PlayerState.DEFAULT

    override fun prepareMediaPlayer(track: Track) {
            mediaPlayer.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    playerState = PlayerState.PREPARED
                }
                setOnCompletionListener {
                    playerState = PlayerState.PREPARED
                }
            }
    }

    override fun startPreviewTrack() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePreviewTrack() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSE
    }

    override fun changePlaybackProgress(): PlayerParams {
        return when (playerState) {
            PlayerState.PLAYING -> {
                PlayerParams(playerState = PlayerState.PLAYING, currentPosition = mediaPlayer.currentPosition)
            }
            PlayerState.PAUSE -> {
                PlayerParams(playerState = PlayerState.PAUSE, currentPosition = mediaPlayer.currentPosition)
            }
            PlayerState.PREPARED -> {
                PlayerParams(playerState = PlayerState.PREPARED, currentPosition = mediaPlayer.currentPosition)
            }
            PlayerState.DEFAULT -> {
                PlayerParams(playerState = PlayerState.DEFAULT, currentPosition = mediaPlayer.currentPosition)
            }
        }

    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}