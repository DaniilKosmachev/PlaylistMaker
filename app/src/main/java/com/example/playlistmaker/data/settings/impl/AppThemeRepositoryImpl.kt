package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.settings.AppThemeRepository
import java.io.Serializable

class AppThemeRepositoryImpl(private var app: App, private var sharedPreferences: SharedPreferences): AppThemeRepository, Serializable {

    override fun getStatusSwitchFromShared(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_SWITCH_SETTINGS, false)

    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        sharedPreferences.edit {
            putBoolean(APP_THEME_SWITCH_SETTINGS, status)
        }
        app.switchTheme(status)
    }

    companion object {
        const val APP_THEME_SWITCH_SETTINGS = "theme_switch_status"
    }

}