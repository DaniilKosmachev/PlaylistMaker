package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.tracks.TrackAdapter
import org.koin.dsl.module

val activityModule = module {

    factory { (tracks: List<Track>, listener: TrackAdapter.trackClickListener) ->
        TrackAdapter(tracks, listener)
    }

}