package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TracksResponse
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.ResponceStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResponceParams

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
            return TrackSearchResponceParams(trackList).apply {
                resultResponse = ResponceStatus.OK
            }
        } else {
            return TrackSearchResponceParams(emptyList()).apply {
                resultResponse = ResponceStatus.BAD
            }
        }
    }

}