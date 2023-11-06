package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackHistoryRepository {
    fun getTrackArrayFromShared(): Array<Track>
    fun writeTrackArrayToShared(tracks: ArrayList<Track>)
    fun clearSearchHistory()
    fun addNewTrackInTrackHistory(newTrack: Track, iTunesTrackSearchHistoryList: ArrayList<Track>)
    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track)
}