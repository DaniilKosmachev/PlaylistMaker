package com.example.playlistmaker.ui.tracks.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel (
    var tracksInteractor: TracksInteractor,
    var trackHistoryInteractor: TrackHistoryInteractor,
): ViewModel() {

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
            mutableListTracks.postValue(emptyList())
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        when (pair.first.isNotEmpty()) {
                            true -> {
                                if (pair.second == ResponceStatus.OK) {
                                    mutableListTracks.postValue(pair.first!!)
                                    mutableStatusActivity.postValue(SearchActivityStatus.Data(pair.first))
                                }
                            }
                            false -> {
                                if (pair.second == ResponceStatus.OK) {
                                    mutableListTracks.postValue(emptyList())
                                    mutableStatusActivity.postValue(SearchActivityStatus.EmptyData())
                                } else if (pair.second == ResponceStatus.BAD) {
                                    mutableListTracks.postValue(emptyList())
                                    mutableStatusActivity.postValue(SearchActivityStatus.ErrorConnection())
                                }
                            }
                        }
                    }
            }

        }
    }


    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
        mutableHistoryListTrack.postValue(readHistorySearchFromShared())
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