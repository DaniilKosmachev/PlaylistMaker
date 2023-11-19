package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface TrackHistoryInteractor {
    fun getTrackArrayFromShared(): Array<Track>
    fun writeTrackArrayToShared(tracks: ArrayList<Track>)
    fun clearSearchHistory()
    fun addNewTrackInTrackHistory(newTrack: Track, iTunesTrackSearchHistoryList: ArrayList<Track>)
    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track)
}