package com.example.playlistmaker.ui.audioplayer.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.search.model.Track

class PlayerActivityViewModel(
    var playerInteractor: PlayerInteractor
): ViewModel() {

    private lateinit var playerParams: PlayerParams

    private var mainTreadHandler = Handler(Looper.getMainLooper())

    private var mutableStatusPlayer = MutableLiveData(PlayerParams(PlayerStatus.DEFAULT,null))

    fun getStatusPlayer(): LiveData<PlayerParams> = mutableStatusPlayer

    fun prepareMediaPlayer(track: Track) {
        playerInteractor.prepareMediaPlayer(track)
        mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PREPARED,null))
    }

    fun startPlay() {
        playerInteractor.startPreviewTrack()
    }

    fun pausePlay() {
        playerInteractor.pausePreviewTrack()
    }

    fun removeCallback() {
        mainTreadHandler.removeCallbacks(checkPlaybackProgressAndStatus())
    }

    fun destroyPlayer() {
        playerInteractor.destroyPlayer()
    }

    fun clickOnPlayButton(track: Track) {
        playerParams = playerInteractor.changePlaybackProgress()
        when (playerParams.playerState) {
            PlayerStatus.PLAYING -> {
                pausePlay()
                mainTreadHandler.post(checkPlaybackProgressAndStatus())
            }

            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> {
                startPlay()
                mainTreadHandler.post(checkPlaybackProgressAndStatus())
            }

            PlayerStatus.DEFAULT -> {
                prepareMediaPlayer(track)
                startPlay()
                mainTreadHandler.post(checkPlaybackProgressAndStatus())
            }
        }
    }

    fun checkPlaybackProgressAndStatus(): Runnable {
        return object : Runnable {
            override fun run() {
                playerParams = playerInteractor.changePlaybackProgress()
                when (playerParams.playerState) {
                    PlayerStatus.PLAYING -> {
                        mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PLAYING, playerParams.currentPosition))
                        mainTreadHandler.postDelayed(this,
                            DELAY_CURRENT_TIME_1000_MILLIS
                        )
                    }
                    PlayerStatus.PAUSE -> {
                        mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PAUSE, playerParams.currentPosition))
                        mainTreadHandler.removeCallbacks(this)
                    }
                    PlayerStatus.PREPARED -> {
                        mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PREPARED, null))
                    }
                    PlayerStatus.DEFAULT -> {
                        mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.DEFAULT, null))
                    }
                }
            }
        }
    }

    companion object {
        private const val DELAY_CURRENT_TIME_1000_MILLIS = 1000L
    }
}