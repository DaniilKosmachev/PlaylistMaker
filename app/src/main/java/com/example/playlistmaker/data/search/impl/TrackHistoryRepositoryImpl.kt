package com.example.playlistmaker.data.search.impl

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson

class TrackHistoryRepositoryImpl(app: Application): TrackHistoryRepository {
     var sharedPreferences: SharedPreferences = app.getSharedPreferences(
         SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME, MODE_PRIVATE)

    override fun getTrackArrayFromShared(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun writeTrackArrayToShared(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_LIST_KEY, json)
            .apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    override fun addNewTrackInTrackHistory(
        newTrack: Track,
        iTunesTrackSearchHistoryList: List<Track>
    ) {
        val temporaryTrackArray = ArrayList<Track>()
        temporaryTrackArray.addAll(iTunesTrackSearchHistoryList)//получаем десериализованный список треков из SP во временный лист
            if (temporaryTrackArray.isEmpty()) {//если он пуст, то просто добавляем новый трек на нулевой индекс
            temporaryTrackArray.add(newTrack)
        } else if (temporaryTrackArray.isNotEmpty()) {
            val iterator: MutableIterator<Track> = temporaryTrackArray.iterator()
            while (iterator.hasNext()) {
                val currentTrack = iterator.next()
                if (currentTrack.trackId == newTrack.trackId) {
                    iterator.remove()
                }
            }
            temporaryTrackArray.add(0, newTrack)
            if (temporaryTrackArray.size == MAX_SIZE_OF_HISTORY_LIST) {
                temporaryTrackArray.removeAt(INDEX_OF_LAST_TRACK_IN_HISTORY_LIST)
            }
        }
        writeTrackArrayToShared(temporaryTrackArray)
    }

    override fun updateHistoryListAfterSelectItemHistoryTrack(track: Track) {
        val temporaryTrackArray = ArrayList<Track>()
        temporaryTrackArray.addAll(getTrackArrayFromShared())
        if (temporaryTrackArray.isNotEmpty()) {
            val iterator: MutableIterator<Track> = temporaryTrackArray.iterator()
            while (iterator.hasNext()) {
                val currentTrack = iterator.next()
                if (currentTrack.trackId == track.trackId) {
                    iterator.remove()
                }
            }
            temporaryTrackArray.add(0, track)
            clearSearchHistory()
            writeTrackArrayToShared(temporaryTrackArray)
        }
    }

    companion object {
       const val SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME = "history_search_track"
        const val HISTORY_TRACK_LIST_KEY = "history_track_list"
        const val MAX_SIZE_OF_HISTORY_LIST = 11
        const val INDEX_OF_LAST_TRACK_IN_HISTORY_LIST = 10
    }
}