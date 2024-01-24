package com.example.playlistmaker.ui.tracks.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.function.Consumer

class SearchViewModel (
    var tracksInteractor: TracksInteractor,
    var trackHistoryInteractor: TrackHistoryInteractor,
): ViewModel(), Consumer<TrackSearchResponceParams> {

    var searchQuery: String = ""

    private var mutableListTracks = MutableLiveData<List<Track>>(emptyList())
    private var mutableStatusActivity = MutableLiveData<SearchActivityStatus>()
    private var mutableHistoryListTrack = MutableLiveData(readHistorySearchFromShared())
    private var mutableIsClickAllowed = MutableLiveData(true)

    private var clickJob: Job? = null
    private var searchJob: Job? = null

    fun getIsClickAllowed(): LiveData<Boolean> {
        onClickAllowed()
        return mutableIsClickAllowed
    }
    fun getHistoryListTrack(): LiveData<List<Track>> = mutableHistoryListTrack
    fun getListTrack(): LiveData<List<Track>> = mutableListTracks
    fun getStatusScreen(): LiveData<SearchActivityStatus> = mutableStatusActivity

    fun readHistorySearchFromShared(): List<Track> {
        return trackHistoryInteractor.getTrackArrayFromShared().toList()
    }


    fun onClickAllowed() {
       if (mutableIsClickAllowed.value != null) {
           clickJob = viewModelScope.launch {
               mutableIsClickAllowed.postValue(false)
               delay(CLICK_ON_TRACK_DELAY_MILLIS)
               mutableIsClickAllowed.postValue(true)
           }
       }
    }

    fun startDelaySearch() {
        mutableStatusActivity.postValue(SearchActivityStatus.Loading())
        searchJob = viewModelScope.launch {
            searchJob?.cancel()
            delay(SEARCH_DELAY_2000_MILLIS)
            startSearchTracks(searchQuery)
        }
    }

    fun removeCallbackSearch() {
        searchJob?.cancel()
        mutableStatusActivity.postValue(SearchActivityStatus.ShowHistory())
        searchQuery = ""
    }

    fun startSearchTracks(query: String) {
        if (query.isNotEmpty()) {
            tracksInteractor.searchTracks(query,this@SearchViewModel)
        }
    }


    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
        mutableHistoryListTrack.postValue(readHistorySearchFromShared())
    }

    override fun accept(t: TrackSearchResponceParams) {
       mutableListTracks.postValue(emptyList())
        when (t.tracks.isNotEmpty()) {
            true -> {
                if (t.resultResponse == ResponceStatus.OK) {
                    mutableListTracks.postValue(t.tracks)
                    mutableStatusActivity.postValue(SearchActivityStatus.Data(t.tracks))
                }
            }
            false -> {
                if (t.resultResponse == ResponceStatus.OK) {
                    mutableListTracks.postValue(emptyList())
                    mutableStatusActivity.postValue(SearchActivityStatus.EmptyData())
                } else if (t.resultResponse == ResponceStatus.BAD) {
                    mutableListTracks.postValue(emptyList())
                    mutableStatusActivity.postValue(SearchActivityStatus.ErrorConnection())
                }
            }
        }
    }

  fun addNewTrackInTrackHistory(track: Track) {
        trackHistoryInteractor.addNewTrackInTrackHistory(track,readHistorySearchFromShared())
        mutableHistoryListTrack.postValue(readHistorySearchFromShared())
    }

    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        trackHistoryInteractor.updateHistoryListAfterSelectItemHistoryTrack(track)
        mutableHistoryListTrack.postValue(readHistorySearchFromShared())
    }

    companion object {
        private const val SEARCH_DELAY_2000_MILLIS = 2000L
        private const val CLICK_ON_TRACK_DELAY_MILLIS = 1000L
    }

}