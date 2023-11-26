package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.search.model.Track
import org.koin.java.KoinJavaComponent.getKoin

class PlayerRepositoryImpl: PlayerRepository {

    private var mediaPlayer: MediaPlayer = getKoin().get()
    private var playerState: PlayerStatus = getKoin().get()

    override fun prepareMediaPlayer(track: Track) {
            mediaPlayer.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    playerState = PlayerStatus.PREPARED
                }
                setOnCompletionListener {
                    playerState = PlayerStatus.PREPARED
                }
            }
    }

    override fun startPreviewTrack() {
        mediaPlayer.start()
        playerState = PlayerStatus.PLAYING
    }

    override fun pausePreviewTrack() {
        mediaPlayer.pause()
        playerState = PlayerStatus.PAUSE
    }

    override fun changePlaybackProgress(): PlayerParams {
            return when (playerState) {
                PlayerStatus.PLAYING -> {
                    PlayerParams(
                        playerState = PlayerStatus.PLAYING,
                        currentPosition = mediaPlayer.currentPosition
                    )
                }

                PlayerStatus.PAUSE -> {
                    PlayerParams(
                        playerState = PlayerStatus.PAUSE,
                        currentPosition = mediaPlayer.currentPosition
                    )
                }

                PlayerStatus.PREPARED -> {
                    PlayerParams(
                        playerState = PlayerStatus.PREPARED,
                        currentPosition = mediaPlayer.currentPosition
                    )
                }

                PlayerStatus.DEFAULT -> {
                    PlayerParams(
                        playerState = PlayerStatus.DEFAULT,
                        currentPosition = mediaPlayer.currentPosition
                    )
                }
            }
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}