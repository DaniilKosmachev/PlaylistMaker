package com.example.playlistmaker.ui.playlist.view_model

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.settings.ExternalSettingsInteractor
import com.example.playlistmaker.domain.settings.model.StatusSharePlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class PlaylistFragmentViewModel(
    private var playlistsInteractor: PlaylistsInteractor,
    private var externalSettingsInteractor: ExternalSettingsInteractor
): ViewModel() {

    private var _StatusSharePlaylist = MutableLiveData<StatusSharePlaylist>()

    fun statusSharePlaylist(): LiveData<StatusSharePlaylist> {
        return _StatusSharePlaylist
    }

    private var _StatusTracksInPlaylist = MutableLiveData<FavoriteTracksState>()

    fun statusTracksInPlaylist(): LiveData<FavoriteTracksState> {
        return _StatusTracksInPlaylist
    }

    private var _StatusUpdatedPlaylist = MutableLiveData<PlaylistsState>()

    fun statusUpdatedPlaylist(): LiveData<PlaylistsState> {
        return _StatusUpdatedPlaylist
    }


    fun selectAllTrackInPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .selectAllTracksInPlaylist(playlistId)
                .collect {
                    tracks ->
                    if (tracks.isNullOrEmpty()) _StatusTracksInPlaylist.postValue(FavoriteTracksState.Empty)
                    else _StatusTracksInPlaylist.postValue(FavoriteTracksState.Content(tracks))
                }
        }
    }

    fun deleteTrackFromDb(trackId: Int, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.removeTrackFromPlaylistTransaction(trackId, playlistId)
            selectAllTrackInPlaylist(playlistId)
        }
    }

    fun sharePlaylist(playlist: Playlist, tracksInPlaylist: ArrayList<Track>) {
        if (tracksInPlaylist.isNotEmpty()) {
            val mainPleylistInfo = playlist.name + "\n${if (playlist.description.isNullOrEmpty()) "Нет описания" else playlist.description}"
            val trackCount = tracksInPlaylist.size
            var listTracks = ""
                tracksInPlaylist.forEach { track -> listTracks += "\n ${tracksInPlaylist.indexOf(track) + 1}. ${track.artistName} - ${track.trackName} (${
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis)
                })"
            }
            externalSettingsInteractor.sharePlaylist(mainPleylistInfo, trackCount, listTracks)
            _StatusSharePlaylist.postValue(StatusSharePlaylist.OK)
        } else {
            _StatusSharePlaylist.postValue(StatusSharePlaylist.ERROR)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.removePlaylistFromDb(playlistId)
        }
    }

    fun updatePlaylistInfo(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .updatePlaylistInfo(playlistId)
                .collect {
                    _StatusUpdatedPlaylist.postValue(PlaylistsState.Content(listOf(it)))
                }
        }
    }


}