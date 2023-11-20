package com.example.playlistmaker.ui.tracks.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchActivityViewModelFactory(var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchActivityViewModel(
            Creator.provideTracksInteractor(),
            Creator.provideTrackHistoryInteractor(),
            context = context
        ) as T
    }
}