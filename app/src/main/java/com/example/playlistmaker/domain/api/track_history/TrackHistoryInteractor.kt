package com.example.playlistmaker.domain.api.track_history

import com.example.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {
    fun getTrackArrayFromShared(): Array<Track>
    fun writeTrackArrayToShared(tracks: ArrayList<Track>)
    fun clearSearchHistory()
    fun addNewTrackInTrackHistory(newTrack: Track, iTunesTrackSearchHistoryList: ArrayList<Track>)
    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track)
}