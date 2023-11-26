package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.ITunesApi
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.search.model.ResponceStatus
import org.koin.java.KoinJavaComponent.getKoin

class RetrofitNetworkClient: NetworkClient {

    private val retrofit: ITunesApi = getKoin().get()

    var emptyResponse: Response = getKoin().get()

    override fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (dto is TrackSearchRequest) {
            try {
                val response = retrofit.search(dto.request).execute()
                val bodyResponse = response.body() ?: emptyResponse
                return bodyResponse.apply { resultResponse = ResponceStatus.OK }
            } catch (e: Exception) {

            }
        }
            return emptyResponse.apply {
                resultResponse = ResponceStatus.BAD
            }
    }
}