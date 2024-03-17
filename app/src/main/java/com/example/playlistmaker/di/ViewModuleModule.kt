package com.example.playlistmaker.di

import com.example.playlistmaker.ui.audioplayer.view_model.PlayerFragmentViewModel
import com.example.playlistmaker.ui.create_playlist.view_model.CreatePlaylistFragmentViewModel
import com.example.playlistmaker.ui.edit_playlist.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.library.view_model.FavoriteTrackFragmentViewModel
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.playlist.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModel = module {

    viewModel {
        PlayerFragmentViewModel(get(), get(), get())
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
        PlaylistsFragmentViewModel(get())
    }

    viewModel {
        CreatePlaylistFragmentViewModel(get())
    }

    viewModel {
        PlaylistFragmentViewModel(get(),get())
    }
    viewModel {
        EditPlaylistViewModel(get())
    }

}