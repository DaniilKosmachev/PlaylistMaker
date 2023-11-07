package com.example.playlistmaker.data.local

import android.app.Application
import com.example.playlistmaker.domain.api.app_theme.AppThemeRepository
import java.io.Serializable

class AppThemeRepositoryImpl(app: Application): AppThemeRepository, Serializable, Application() {
    var sharedPreferences = app.getSharedPreferences(APP_THEME_SHARED_FILE_NAME, MODE_PRIVATE)

    override fun getStatusSwitchFromShared(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_SWITCH_SETTINGS, false)
    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_SWITCH_SETTINGS, status)
            .apply()
    }

    companion object {
        const val APP_THEME_SHARED_FILE_NAME = "theme_shared_preferences"
        const val APP_THEME_SWITCH_SETTINGS = "theme_switch_status"
    }

}