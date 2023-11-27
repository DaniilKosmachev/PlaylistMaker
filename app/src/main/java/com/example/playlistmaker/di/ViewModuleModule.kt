package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.view_model.PlayerActivityViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.tracks.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModel = module {

    viewModel {
        PlayerActivityViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchActivityViewModel(get(),get())
    }

}