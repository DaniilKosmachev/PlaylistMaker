package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.AppThemeInteractor
import com.example.playlistmaker.domain.settings.AppThemeRepository

class AppThemeInteractorImpl(private val appThemeSharedPreferences: AppThemeRepository): AppThemeInteractor {

    override fun getStatusSwitchFromShared(): Boolean {
        return appThemeSharedPreferences.getStatusSwitchFromShared()
    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        appThemeSharedPreferences.writeStatusSwitchToShared(status)
    }

}