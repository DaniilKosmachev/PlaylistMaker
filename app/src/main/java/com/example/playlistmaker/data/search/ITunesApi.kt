package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}