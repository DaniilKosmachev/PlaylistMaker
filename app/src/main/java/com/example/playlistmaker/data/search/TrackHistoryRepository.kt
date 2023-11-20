package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.model.Track

interface TrackHistoryRepository {
    fun getTrackArrayFromShared(): Array<Track>
    fun writeTrackArrayToShared(tracks: ArrayList<Track>)
    fun clearSearchHistory()
    fun addNewTrackInTrackHistory(newTrack: Track, iTunesTrackSearchHistoryList: List<Track>)
    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track)
}