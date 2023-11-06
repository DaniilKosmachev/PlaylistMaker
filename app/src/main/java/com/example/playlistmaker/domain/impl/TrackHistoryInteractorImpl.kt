package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.models.Track

class TrackHistoryInteractorImpl(private val historySharedRepository: TrackHistoryRepository): TrackHistoryInteractor {
    override fun getTrackArrayFromShared(): Array<Track> {
        return historySharedRepository.getTrackArrayFromShared()
    }

    override fun writeTrackArrayToShared(tracks: ArrayList<Track>) {
        historySharedRepository.writeTrackArrayToShared(tracks)
    }

    override fun clearSearchHistory() = historySharedRepository.clearSearchHistory()

    override fun addNewTrackInTrackHistory(
        newTrack: Track,
        iTunesTrackSearchHistoryList: ArrayList<Track>
    ) {
        historySharedRepository.addNewTrackInTrackHistory(newTrack, iTunesTrackSearchHistoryList)
    }

    override fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        historySharedRepository.updateHistoryListAfterSelectItemHistoryTrack(track)
    }
}