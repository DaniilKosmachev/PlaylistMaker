package com.example.playlistmaker.ui.tracks.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import java.util.function.Consumer

class SearchActivityViewModel (
    var tracksInteractor: TracksInteractor,
    var trackHistoryInteractor: TrackHistoryInteractor,
): ViewModel(), Consumer<TrackSearchResponceParams> {

    var searchQuery: String = ""

    private var mutableListTracks = MutableLiveData<List<Track>>(emptyList())
    private var mutableStatusActivity = MutableLiveData<SearchActivityStatus>()
    private var mutableHistoryListTrack = MutableLiveData(readHistorySearchFromShared())
    private var mutableIsClickAllowed = MutableLiveData(true)

    private var mainTreadHandler = Handler(Looper.getMainLooper())

    private var startMainSearch = Runnable {
        startSearchTracks(searchQuery)
    }

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
           mutableIsClickAllowed.postValue(false)
           mainTreadHandler.postDelayed({ mutableIsClickAllowed.postValue(true) }, CLICK_ON_TRACK_DELAY_MILLIS)
       }
    }

    fun startDelaySearch() {
        mutableStatusActivity.postValue(SearchActivityStatus.Loading())
        mainTreadHandler.removeCallbacks(startMainSearch)
        mainTreadHandler.postDelayed(startMainSearch, SEARCH_DELAY_2000_MILLIS)
    }

    fun removeCallbackSearch() {
        mainTreadHandler.removeCallbacks(startMainSearch)
        mutableStatusActivity.postValue(SearchActivityStatus.ShowHistory())
        searchQuery = ""
    }

    fun startSearchTracks(query: String) {
        if (query.isNotEmpty()) {
            tracksInteractor.searchTracks(query,this@SearchActivityViewModel)
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