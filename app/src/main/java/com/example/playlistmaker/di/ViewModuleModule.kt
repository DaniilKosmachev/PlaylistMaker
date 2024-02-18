package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.view_model.PlayerActivityViewModel
import com.example.playlistmaker.ui.library.view_model.FavoriteTrackFragmentViewModel
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModel = module {

    viewModel {
        PlayerActivityViewModel(get(), get(),get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchViewModel(get(),get())
    }

    viewModel {
        FavoriteTrackFragmentViewModel(get())
    }

    viewModel {
        PlaylistsFragmentViewModel()
    }

}