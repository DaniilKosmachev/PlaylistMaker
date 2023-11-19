package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.TrackHistoryRepository
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.data.search.impl.RetrofitNetworkClient
import com.example.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.AppThemeRepository
import com.example.playlistmaker.data.settings.ExternalSettingsRepository
import com.example.playlistmaker.data.settings.impl.AppThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ExternalSettingsRepositoryImpl
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

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor() : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTrackHistoryRepository() : TrackHistoryRepository {
        return TrackHistoryRepositoryImpl(application)
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }

    private fun getAppThemeRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(application)
    }

    fun provideAppThemeInteractor(): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
     private fun getExternalSettingsRepository(context: Context): ExternalSettingsRepository {
         return ExternalSettingsRepositoryImpl(context)
     }

    fun provideExternalSettingsInteractor(context: Context): ExternalSettingsInteractor {
        return ExternalSettingsInteractorImpl(getExternalSettingsRepository(context))
    }

}