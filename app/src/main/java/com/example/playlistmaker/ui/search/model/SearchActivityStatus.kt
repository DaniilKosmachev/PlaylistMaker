package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.search.model.Track

sealed class SearchActivityStatus {
    class Data(var data: List<Track>): SearchActivityStatus()
    class EmptyData(): SearchActivityStatus()
    class ErrorConnection(): SearchActivityStatus()
    class Loading(): SearchActivityStatus()
    class ShowHistory(): SearchActivityStatus()
}