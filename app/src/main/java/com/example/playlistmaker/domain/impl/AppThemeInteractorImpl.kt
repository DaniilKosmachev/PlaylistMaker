package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.app_theme.AppThemeInteractor
import com.example.playlistmaker.domain.api.app_theme.AppThemeRepository

class AppThemeInteractorImpl(private val appThemeSharedPreferences: AppThemeRepository): AppThemeInteractor {
    override fun getStatusSwitchFromShared(): Boolean {
        return appThemeSharedPreferences.getStatusSwitchFromShared()
    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        appThemeSharedPreferences.writeStatusSwitchToShared(status)
    }

}