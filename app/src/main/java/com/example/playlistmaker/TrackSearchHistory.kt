package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class TrackSearchHistory(sharedPreferences: SharedPreferences) { //класс для работы с SharedPreferences истории поиска

    companion object {
        const val HISTORY_TRACK_LIST_KEY = "history_track_list"
    }

    private val sharedPreferencesHistory = sharedPreferences

    fun getTrackArrayFromShared(): Array<Track> {
        val json = sharedPreferencesHistory.getString(HISTORY_TRACK_LIST_KEY,null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun writeTrackArrayToShared(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferencesHistory.edit()
            .putString(HISTORY_TRACK_LIST_KEY,json)
            .apply()
    }

    fun clearSearchHistory() = sharedPreferencesHistory.edit()
        .clear()
        .apply()
}