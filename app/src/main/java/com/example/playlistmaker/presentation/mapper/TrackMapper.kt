package com.example.playlistmaker.presentation.mapper

import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TrackMapper {
    fun getLocalDateTime(track: Track): String {
        return LocalDateTime.parse(track.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).year.toString()
    }

    fun getSimpleDateFormat(track: Track): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }
    fun getCoverArtWork(track: Track): String = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}