package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.settings.AppThemeRepository
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import java.io.Serializable

class AppThemeRepositoryImpl: AppThemeRepository, Serializable {

    var app: App= getKoin().get(named("context"))

    var sharedPreferences: SharedPreferences = getKoin().get(named("themeShared"))

    override fun getStatusSwitchFromShared(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_SWITCH_SETTINGS, false)

    }

    override fun writeStatusSwitchToShared(status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(APP_THEME_SWITCH_SETTINGS, status)
            .apply()
        app.switchTheme(status)
    }

    companion object {
        const val APP_THEME_SWITCH_SETTINGS = "theme_switch_status"
    }

}