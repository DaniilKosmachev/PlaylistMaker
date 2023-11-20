package com.example.playlistmaker.ui.tracks.view_model

import android.content.Context
import android.content.Intent
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
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import java.util.function.Consumer

class SearchActivityViewModel (
    var tracksInteractor: TracksInteractor,
    var trackHistoryInteractor: TrackHistoryInteractor,
    var context: Context
): ViewModel(), Consumer<TrackSearchResponceParams> {

    var searchQuery: String = ""

    private var mutableListTracks = MutableLiveData<List<Track>>(emptyList())
    private var mutableStatusActivity = MutableLiveData<SearchActivityStatus>()
    private var mutableHistoryListTrack = MutableLiveData(readHistorySearchFromShared())
    private var mutableIsClickAllowed = MutableLiveData<Boolean>(true)

    private var mainTreadHandler = Handler(Looper.getMainLooper())

    fun getIsClickAllowed(): LiveData<Boolean> = mutableIsClickAllowed
    fun getHistoryListTrack(): LiveData<List<Track>> = mutableHistoryListTrack
    fun getListTrack(): LiveData<List<Track>> = mutableListTracks
    fun getStatusScreen(): LiveData<SearchActivityStatus> = mutableStatusActivity

    fun readHistorySearchFromShared(): List<Track> {
        return trackHistoryInteractor.getTrackArrayFromShared().toList()
    }

    fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        Intent(context, AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
            context.startActivity(this)
        }
    }

    fun onClickAllowed() {
        val current = mutableIsClickAllowed
       if (mutableIsClickAllowed.value!!) {
           mutableIsClickAllowed.postValue(false)
           mainTreadHandler.postDelayed({ mutableIsClickAllowed.postValue(true) }, CLICK_ON_TRACK_DELAY_MILLIS)
       }
    }

    fun startDelaySearch() {
        mutableStatusActivity.postValue(SearchActivityStatus.Loading())
        mainTreadHandler.removeCallbacks(searchTracks())
        mainTreadHandler.postDelayed(searchTracks(), SEARCH_DELAY_2000_MILLIS)
    }

    fun removeCallbackSearch() {
        mainTreadHandler.removeCallbacks(searchTracks())
        mutableStatusActivity.postValue(SearchActivityStatus.ShowHistory())
        searchQuery = ""
    }

    fun searchTracks() : Runnable {
        return Runnable {
                if (!searchQuery.isNullOrEmpty()) {
                    tracksInteractor.searchTracks(searchQuery,this)
                }
            }
        }

    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
        mutableHistoryListTrack.postValue(readHistorySearchFromShared())
    }

    override fun accept(t: TrackSearchResponceParams) {
       mutableListTracks.postValue(emptyList())
        if (t.tracks.isNotEmpty() && t.resultResponse == ResponceStatus.OK) {
            mutableListTracks.postValue(t.tracks)
            mutableStatusActivity.postValue(SearchActivityStatus.Data(t.tracks))
        } else if (t.tracks.isEmpty() && t.resultResponse == ResponceStatus.OK) {
            mutableListTracks.postValue(emptyList())
            mutableStatusActivity.postValue(SearchActivityStatus.EmptyData())
        } else if (t.tracks.isEmpty() && t.resultResponse == ResponceStatus.BAD) {
            mutableListTracks.postValue(emptyList())
            mutableStatusActivity.postValue(SearchActivityStatus.ErrorConnection())
        }
    }

  fun addNewTrackInTrackHistory(track: Track) {
        trackHistoryInteractor.addNewTrackInTrackHistory(track,readHistorySearchFromShared())
    }

    fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        trackHistoryInteractor.updateHistoryListAfterSelectItemHistoryTrack(track)
    }

    companion object {
        private const val SEARCH_DELAY_2000_MILLIS = 2000L
        private const val CLICK_ON_TRACK_DELAY_MILLIS = 1000L
    }

}