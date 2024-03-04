package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.model.SearchActivityStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel (
    var tracksInteractor: TracksInteractor,
    var trackHistoryInteractor: TrackHistoryInteractor,
): ViewModel() {

    var searchQuery: String = ""

    private var _ListTracks = MutableLiveData<List<Track>>(emptyList())
    private var _StatusActivity = MutableLiveData<SearchActivityStatus>()
    private var _HistoryListTrack = MutableLiveData(readHistorySearchFromShared())
    private var _IsClickAllowed = MutableLiveData(true)

    private var clickJob: Job? = null
    private var searchJob: Job? = null

    fun getIsClickAllowed(): LiveData<Boolean> {
        onClickAllowed()
        return _IsClickAllowed
    }
    fun getHistoryListTrack(): LiveData<List<Track>> = _HistoryListTrack
    fun getListTrack(): LiveData<List<Track>> = _ListTracks
    fun getStatusScreen(): LiveData<SearchActivityStatus> = _StatusActivity

    fun readHistorySearchFromShared(): List<Track> {
        return trackHistoryInteractor.getTrackArrayFromShared().toList()
    }


    fun onClickAllowed() {
       if (_IsClickAllowed.value != null) {
           clickJob = viewModelScope.launch {
               _IsClickAllowed.postValue(false)
               delay(CLICK_ON_TRACK_DELAY_MILLIS)
               _IsClickAllowed.postValue(true)
           }
       }
    }

    fun startDelaySearch() {
        _StatusActivity.postValue(SearchActivityStatus.Loading())
        searchJob = viewModelScope.launch {
            searchJob?.cancel()
            delay(SEARCH_DELAY_2000_MILLIS)
            startSearchTracks(searchQuery)
        }
    }

    fun removeCallbackSearch() {
        searchJob?.cancel()
        _StatusActivity.postValue(SearchActivityStatus.ShowHistory())
        searchQuery = ""
    }

    fun startSearchTracks(query: String) {
        if (query.isNotEmpty()) {
            _ListTracks.postValue(emptyList())
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        when (pair.first.isNotEmpty()) {
                            true -> {
                                if (pair.second == ResponceStatus.OK) {
                                    _ListTracks.postValue(pair.first!!)
                                    _StatusActivity.postValue(SearchActivityStatus.Data(pair.first))
                                }
                            }
                            false -> {
                                if (pair.second == ResponceStatus.OK) {
                                    _ListTracks.postValue(emptyList())
                                    _StatusActivity.postValue(SearchActivityStatus.EmptyData())
                                } else if (pair.second == ResponceStatus.BAD) {
                                    _ListTracks.postValue(emptyList())
                                    _StatusActivity.postValue(SearchActivityStatus.ErrorConnection())
                                }
                            }
                        }
                    }
            }

        }
    }


    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
        _HistoryListTrack.postValue(readHistorySearchFromShared())
    }

  fun addNewTrackInTrackHistory(track: Track) {
        trackHistoryInteractor.addNewTrackInTrackHistory(track,readHistorySearchFromShared())
        _HistoryListTrack.postValue(readHistorySearchFromShared())
    }

    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        trackHistoryInteractor.updateHistoryListAfterSelectItemHistoryTrack(track)
        _HistoryListTrack.postValue(readHistorySearchFromShared())
    }

    companion object {
        private const val SEARCH_DELAY_2000_MILLIS = 2000L
        private const val CLICK_ON_TRACK_DELAY_MILLIS = 1000L
    }

}