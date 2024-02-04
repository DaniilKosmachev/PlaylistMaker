package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.Track

class TrackDbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.trackId, track.artworkUrl100, track.trackName, track.artistName, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.trackTimeMillis, track.previewUrl)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.id, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }
}