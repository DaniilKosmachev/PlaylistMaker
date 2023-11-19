package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.AppThemeRepository
import com.example.playlistmaker.domain.settings.AppThemeInteractor

class AppThemeInteractorImpl(private val appThemeSharedPreferences: AppThemeRepository):
    AppThemeInteractor {
    override fun getStatusSwitchFromShared(): Boolean {
        return appThemeSharedPreferences.getStatusSwitchFromShared()
    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        appThemeSharedPreferences.writeStatusSwitchToShared(status)
    }

}