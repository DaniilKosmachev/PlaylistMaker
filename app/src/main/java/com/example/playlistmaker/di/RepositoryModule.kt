package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.impl.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.data.db.impl.PlaylistsRepositoryImpl
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.impl.AppThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ExternalSettingsRepositoryImpl
import com.example.playlistmaker.domain.db.FavouriteTracksRepository
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.AppThemeRepository
import com.example.playlistmaker.domain.settings.ExternalSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

var repositoryModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(named("historyShared")), get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory<AppThemeRepository> {
        AppThemeRepositoryImpl(get(named("context")), get(named("themeShared")))
    }

    factory<ExternalSettingsRepository> {
        ExternalSettingsRepositoryImpl(androidContext())
    }

    factory {
        TrackDbConverter()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get(), get())
    }

    factory {
        PlaylistDbConverter(get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(),get())
    }

}