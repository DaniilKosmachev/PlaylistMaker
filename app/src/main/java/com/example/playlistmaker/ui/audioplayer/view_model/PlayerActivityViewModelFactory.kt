package com.example.playlistmaker.ui.audioplayer.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class PlayerActivityViewModelFactory(context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerActivityViewModel(
            Creator.providePlayerInteractor()
        ) as T
    }
}