package com.example.playlistmaker.domain.search.model

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.model.Track

data class TrackSearchResponceParams(var tracks: List<Track>): Response()
