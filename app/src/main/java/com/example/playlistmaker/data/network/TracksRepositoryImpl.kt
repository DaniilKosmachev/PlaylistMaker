package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.api.track.TracksRepository
import com.example.playlistmaker.domain.models.ResponceStatus
import com.example.playlistmaker.domain.models.TrackSearchResponceParams

class TracksRepositoryImpl (private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): TrackSearchResponceParams {
        val response = networkClient.doTrackSearchRequest(TrackSearchRequest(expression))
        if (response.resultResponse == ResponceStatus.OK) {
            var trackList: List<Track> = (response as TracksResponse).results.map {
                Track(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    trackId = it.trackId,
                    collectionName = it.collectionName,
                    releaseDate = if (it.releaseDate.isNullOrEmpty()) {"Нет данных"} else it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = if (it.previewUrl.isNullOrEmpty()) {"Нет данных"} else it.previewUrl
                )
            }
            var responceParams: TrackSearchResponceParams = TrackSearchResponceParams(trackList)
            responceParams.resultResponse = ResponceStatus.OK
            return responceParams
        } else {
            var responceParams: TrackSearchResponceParams = TrackSearchResponceParams(emptyList())
            responceParams.resultResponse = ResponceStatus.BAD
            return responceParams
        }
    }

}