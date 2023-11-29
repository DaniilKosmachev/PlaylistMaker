package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.impl.AppThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ExternalSettingsRepositoryImpl
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
        PlayerRepositoryImpl(get()/*,get()*/)
    }

    factory<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(named("historyShared")), get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    factory<AppThemeRepository> {
        AppThemeRepositoryImpl(get(named("context")), get(named("themeShared")))
    }

    factory<ExternalSettingsRepository> {
        ExternalSettingsRepositoryImpl(androidContext())
    }

}