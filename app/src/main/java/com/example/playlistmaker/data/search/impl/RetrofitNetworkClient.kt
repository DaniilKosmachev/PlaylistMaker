package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.ITunesApi
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.search.model.ResponceStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val retrofit: ITunesApi): NetworkClient {

    override suspend fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
            retrofit.search(dto.request).apply {
                resultResponse = ResponceStatus.OK
            } ?: Response(ResponceStatus.BAD)
        } catch (e: Exception) {
            Response(ResponceStatus.BAD)
        }
    }
}
}