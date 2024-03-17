package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlayerParams
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.player.model.TracksInPlaylists
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerFragmentViewModel(
    var playerInteractor: PlayerInteractor,
    var favouriteTracksInteractor: FavouriteTracksInteractor,
    var playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var progressJob: Job? = null

    private var playerParams: PlayerParams? = null

    private var _IsFavoriteTrack = MutableLiveData<Boolean>()

    private var _StatusPlayer = MutableLiveData(PlayerParams(PlayerStatus.DEFAULT,null))

    private var _PlaylistStatus = MutableLiveData<PlaylistsState>()

    private var _StatusCheckTrackInPlaylist = MutableLiveData<List<Int>>()

    fun getStatusCheckTrackInPlaylists(): LiveData<List<Int>> = _StatusCheckTrackInPlaylist

    fun getPlaylistStatus(): LiveData<PlaylistsState> = _PlaylistStatus

    fun getStatusPlayer(): LiveData<PlayerParams> = _StatusPlayer

    fun getIsFavoriteTrack(): LiveData<Boolean> = _IsFavoriteTrack

    fun prepareMediaPlayer(track: Track) {
        playerInteractor.prepareMediaPlayer(track)
        _StatusPlayer.postValue(PlayerParams(PlayerStatus.PREPARED,null))
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
        when (playerParams!!.playerState) {
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
        when (playerParams!!.playerState) {
            PlayerStatus.PLAYING -> {
                _StatusPlayer.postValue(PlayerParams(PlayerStatus.PLAYING, playerParams!!.currentPosition))
                progressJob = viewModelScope.launch {
                    delay(DELAY_CURRENT_TIME_300_MILLIS)
                    checkPlaybackProgressAndStatus()
                }
            }
            PlayerStatus.PAUSE -> {
                _StatusPlayer.postValue(PlayerParams(PlayerStatus.PAUSE, playerParams!!.currentPosition))
                progressJob?.cancel()
            }
            PlayerStatus.PREPARED -> {
                _StatusPlayer.postValue(PlayerParams(PlayerStatus.PREPARED, null))
            }
            PlayerStatus.DEFAULT -> {
                _StatusPlayer.postValue(PlayerParams(PlayerStatus.DEFAULT, null))
            }
        }
    }

    fun onFavoriteClicked(track: Track) {

        when (track.isFavorite) {
            true -> {
                viewModelScope.launch(Dispatchers.IO) {
                    favouriteTracksInteractor.deleteTrackInDbFavourite(track)
                }
                track.isFavorite = false
                _IsFavoriteTrack.postValue(false)
            }
            false -> {
                viewModelScope.launch(Dispatchers.IO) {
                    favouriteTracksInteractor.deleteTrackInDbFavourite(track)
                    track.isFavorite = true
                    favouriteTracksInteractor.addTrackInDbFavourite(track)
                }
                _IsFavoriteTrack.postValue(true)
            }
        }
    }

    fun selectAllPlaylistsFromDb() {
       viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .selectAllPlaylists()
                .collect { playlists ->
                when(playlists.isEmpty()) {
                    true -> _PlaylistStatus.postValue(PlaylistsState.Empty)
                    false -> _PlaylistStatus.postValue(PlaylistsState.Content(playlists))
                }

                }
        }


    }

    fun selectableTrackIsInPLaylist(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .checkTrackInPlaylist(trackId)
                .collect { trackInDb ->
                    when(trackInDb.size) {
                        0 -> _StatusCheckTrackInPlaylist.postValue(emptyList())
                        else -> _StatusCheckTrackInPlaylist.postValue(trackInDb)
                    }

                }
        }
    }

    fun addNewTrackInPlaylistTransaction(tracksInPlaylists: TracksInPlaylists, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.addNewTrackInPlaylistsTransaction(tracksInPlaylists, playlistId)
        }
    }
    companion object {
        private const val DELAY_CURRENT_TIME_300_MILLIS = 300L
    }
}