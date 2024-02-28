package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavouriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.db.impl.FavouriteTracksInteractorImpl
import com.example.playlistmaker.domain.db.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.AppThemeInteractor
import com.example.playlistmaker.domain.settings.ExternalSettingsInteractor
import com.example.playlistmaker.domain.settings.impl.AppThemeInteractorImpl
import com.example.playlistmaker.domain.settings.impl.ExternalSettingsInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executors

val interactorModule = module {

    factory<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<ExternalSettingsInteractor> {
        ExternalSettingsInteractorImpl(get())
    }

    factory<AppThemeInteractor> {
        AppThemeInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory {
        Executors.newCachedThreadPool()
    }

    factory<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(get())
    }

    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

}