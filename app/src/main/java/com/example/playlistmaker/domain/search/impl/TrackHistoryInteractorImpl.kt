package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.model.Track

class TrackHistoryInteractorImpl(private val historySharedRepository: TrackHistoryRepository):
    TrackHistoryInteractor {
    override fun getTrackArrayFromShared(): Array<Track> {
        return historySharedRepository.getTrackArrayFromShared()
    }

    override fun writeTrackArrayToShared(tracks: ArrayList<Track>) {
        historySharedRepository.writeTrackArrayToShared(tracks)
    }

    override fun clearSearchHistory() = historySharedRepository.clearSearchHistory()

    override fun addNewTrackInTrackHistory(
        newTrack: Track,
        iTunesTrackSearchHistoryList: List<Track>
    ) {
        historySharedRepository.addNewTrackInTrackHistory(newTrack, iTunesTrackSearchHistoryList)
    }

    override fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        historySharedRepository.updateHistoryListAfterSelectItemHistoryTrack(track)
    }
}