package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.data.local.AppThemeRepositoryImpl
import com.example.playlistmaker.data.local.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.app_theme.AppThemeInteractor
import com.example.playlistmaker.domain.api.app_theme.AppThemeRepository
import com.example.playlistmaker.domain.api.track.TracksInteractor
import com.example.playlistmaker.domain.api.track.TracksRepository
import com.example.playlistmaker.domain.api.track_history.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.track_history.TrackHistoryRepository
import com.example.playlistmaker.domain.impl.AppThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
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

}