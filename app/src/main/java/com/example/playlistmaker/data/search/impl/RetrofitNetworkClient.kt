package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.ITunesApi
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.search.model.ResponceStatus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (dto is TrackSearchRequest) {
            try {
                val response = itunesService.search(dto.request).execute()
                val bodyResponse = response.body() ?: Response()
                return bodyResponse.apply { resultResponse = ResponceStatus.OK }
            } catch (e: Exception) {

            }
        }
            return Response().apply {
                resultResponse = ResponceStatus.BAD
            }
    }

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}