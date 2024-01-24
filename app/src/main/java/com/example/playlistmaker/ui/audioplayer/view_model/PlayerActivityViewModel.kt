package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerActivityViewModel(
    var playerInteractor: PlayerInteractor
): ViewModel() {

    private var progressJob: Job? = null

    private lateinit var playerParams: PlayerParams

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
        progressJob?.cancel()
    }

    fun destroyPlayer() {
        playerInteractor.destroyPlayer()
    }

    fun clickOnPlayButton(track: Track) {
        playerParams = playerInteractor.changePlaybackProgress()
        when (playerParams.playerState) {
            PlayerStatus.PLAYING -> {
                pausePlay()
                checkPlaybackProgressAndStatus()
            }

            PlayerStatus.PAUSE, PlayerStatus.PREPARED -> {
                startPlay()
                checkPlaybackProgressAndStatus()
            }

            PlayerStatus.DEFAULT -> {
                prepareMediaPlayer(track)
                startPlay()
                checkPlaybackProgressAndStatus()
            }
        }
    }

    fun checkPlaybackProgressAndStatus() {
        playerParams = playerInteractor.changePlaybackProgress()
        when (playerParams.playerState) {
            PlayerStatus.PLAYING -> {
                mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PLAYING, playerParams.currentPosition))
                progressJob = viewModelScope.launch {
                    delay(DELAY_CURRENT_TIME_300_MILLIS)
                    checkPlaybackProgressAndStatus()
                }
            }
            PlayerStatus.PAUSE -> {
                mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PAUSE, playerParams.currentPosition))
                progressJob?.cancel()
            }
            PlayerStatus.PREPARED -> {
                mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.PREPARED, null))
            }
            PlayerStatus.DEFAULT -> {
                mutableStatusPlayer.postValue(PlayerParams(PlayerStatus.DEFAULT, null))
            }
        }
    }

    companion object {
        private const val DELAY_CURRENT_TIME_300_MILLIS = 300L
    }
}