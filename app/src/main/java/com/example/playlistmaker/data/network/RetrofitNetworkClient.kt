package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
                val response = itunesService.search(dto.request).execute()
                val bodyResponse = response.body() ?: Response()
                bodyResponse.apply {
                    resultResponse = response.code()
                }
            } else {
            return Response().apply {
                resultResponse = BAD_RESPONSE_CODE
            }
        }

    }

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val BAD_RESPONSE_CODE = 400
    }
}